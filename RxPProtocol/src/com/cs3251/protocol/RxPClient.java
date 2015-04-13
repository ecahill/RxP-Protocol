package com.cs3251.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.cs3251.RxPPacket;
import com.cs3251.RxPPacketHeader;

public class RxPClient {
	private DatagramSocket clientSocket;
	private DatagramPacket sendPacket;
	private DatagramPacket recvPacket;
	private String sourceIP;
	private String destIP;
	private short sourcePort;
	private short destPort;
	private int connectionState;
	private RxPClientPacketFactory packetFactory;

	private RxPPacket packetSent;
	private RxPPacket packetRecv;
	
	public RxPClient(String sourceIP, String destIP, short sourcePort, short destPort){
		this.sourceIP = sourceIP;
		this.sourcePort = sourcePort;
		this.destIP = destIP;
		this.destPort = destPort;
		this.connectionState = 0;
		packetFactory = new RxPClientPacketFactory();
	}
	
	public int connect() throws IOException, ClassNotFoundException{
		if(connectionState != 0) return -1;
		clientSocket = new DatagramSocket(sourcePort);
		packetSent = packetFactory.createConnectionPacket(sourceIP, destIP, destPort, sourcePort);
		sendPacket(packetSent);
		System.out.println(packetSent.toString());
		packetRecv = recvPacket(packetSent);
		System.out.println(packetRecv.toString());
		if(packetRecv.getPacketHeader().getAckNumber() != (packetSent.getPacketHeader().getSeqNumber() + 1)) return -1;
		packetSent = packetFactory.createNextPacket(packetRecv);
		sendPacket(packetSent);
		System.out.println(packetSent.toString());
		packetRecv = recvPacket(packetSent);
		System.out.println(packetRecv.toString());
		connectionState = packetRecv.getPacketHeader().getConnectionCode();
		return connectionState;
	}
	
	public int sendData(byte[] data) throws IOException, ClassNotFoundException{
		if(connectionState != 201) return -1;

		packetSent = packetFactory.createPutRequestPacket(sourceIP, destIP, destPort, sourcePort, data.length);
		sendPacket(packetSent);
		packetRecv = recvPacket(packetSent);
		if(packetRecv.getPacketHeader().getConnectionCode() != 501) return -1;		
		
		int dataPosition = 0;
		RxPPacketHeader sendDataHeader = new RxPPacketHeader();
		sendDataHeader.setSourceIP(sourceIP);
		sendDataHeader.setDestIP(destIP);
		sendDataHeader.setSourcePort(sourcePort);
		sendDataHeader.setDestPort(destPort);
		sendDataHeader.setSeqNumber(dataPosition);
		sendDataHeader.setAckNumber(0);
		sendDataHeader.setChecksum(0);
		sendDataHeader.setDataSize(data.length);
		sendDataHeader.setPacketSize( (512 - sendDataHeader.getHeaderSize()) >= data.length ? data.length : 512 - sendDataHeader.getHeaderSize());
		packetSent.setRxPPacketHeader(sendDataHeader);		
		while(dataPosition < data.length){
			packetSent.setData(Arrays.copyOfRange(data, dataPosition, packetSent.getPacketHeader().getPacketSize()));
			sendPacket(packetSent);
			
			//recv here
			//check ack and send those packets
			
			packetRecv = recvPacket(packetSent);
			
			
			dataPosition += packetSent.getPacketHeader().getPacketSize();
			sendDataHeader.setPacketSize( (512 - sendDataHeader.getHeaderSize()) >= (data.length - dataPosition) ? (data.length - dataPosition) : 512 - sendDataHeader.getHeaderSize());
			packetSent.setRxPPacketHeader(sendDataHeader);
		}
		return 0;
	}
	
	private void sendPacket(RxPPacket packetToSend) throws IOException{
		byte[] bytesToSend = new byte[512];
		byte[] packetHeader = packetToSend.getPacketHeader().headerToByte();
		System.arraycopy(packetHeader, 0, bytesToSend, 0, packetHeader.length);
		if(packetToSend.getPacketHeader().getPacketSize() != 0){
			System.arraycopy(packetToSend.getData(), 0, bytesToSend, packetHeader.length, packetToSend.getPacketHeader().getPacketSize());
		}
		sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(packetToSend.getPacketHeader().getDestIP()), packetToSend.getPacketHeader().getDestPort());
		clientSocket.send(sendPacket);
	}
	
	private RxPPacket recvPacket(RxPPacket lastPacketSent) throws IOException, ClassNotFoundException{
		byte[] recv = new byte[512];
		RxPPacket newRecvdPacket;
		recvPacket = new DatagramPacket(recv, recv.length);
		clientSocket.receive(recvPacket);
		newRecvdPacket = new RxPPacket();
		newRecvdPacket.setRxPPacketHeader(recv);
		//add data here if any? or perhaps somewhere else?
		return newRecvdPacket;
	}
	
	public void close() {
		if(!clientSocket.isClosed()){
			clientSocket.close();
			connectionState = 0;
		}
		
	}
	
}
