package com.cs3251.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.cs3251.RxPPacket;

public class RxPServer {

	private DatagramSocket serverSocket;
	private DatagramPacket sendPacket;
	private DatagramPacket recvPacket;
	private String sourceIP;
	private String destIP;
	private short sourcePort;
	private short destPort;
	private int connectionState;
	private RxPServerPacketFactory packetFactory;
	private int windowSize;
	
	private RxPPacket packetSent;
	private RxPPacket packetRecv;
	
	public RxPServer(String sourceIP, String destIP, short sourcePort, short destPort){
		this.sourceIP = sourceIP;
		this.sourcePort = sourcePort;
		this.destIP = destIP;
		this.destPort = destPort;
		this.connectionState = 0;
		packetFactory = new RxPServerPacketFactory();
	}
	
	public int startRxPServer() throws IOException, ClassNotFoundException{
		serverSocket = new DatagramSocket(sourcePort);
		packetSent = packetFactory.createConnectionPacket(sourceIP, destIP, destPort, sourcePort);
		System.out.println("Awaiting connection...");
		packetRecv = recvPacket(packetSent);
		System.out.println(packetRecv.toString());
		packetSent = packetFactory.createNextPacket(packetRecv);
		sendPacket(packetSent);
		System.out.println(packetSent.toString());
		packetRecv = recvPacket(packetSent);
		System.out.println(packetRecv.toString());
		if(packetRecv.getPacketHeader().getAckNumber() != packetSent.getPacketHeader().getSeqNumber() + 1) return -1;
		packetSent = packetFactory.createNextPacket(packetRecv);
		System.out.println(packetSent.toString());
		sendPacket(packetSent);
		connectionState = packetSent.getPacketHeader().getConnectionCode();
		return connectionState;
	}	
	
	public byte[] runServer() throws ClassNotFoundException, IOException{
		if(connectionState != 201) return null;
		packetRecv = recvPacket(packetSent);
		if(packetRecv.getPacketHeader().getConnectionCode() == 500){
			return clientSendRequestHandler();
		}
		if(packetRecv.getPacketHeader().getConnectionCode() == 400){
			return clientSendRequestHandler();
		}
		
		//recv packet and check the connection code for the reqest
		//based on the request, make functions for this
		
		
		return null;
	}
	
	private byte[] clientSendRequestHandler() throws IOException, ClassNotFoundException{
		packetSent = packetFactory.createNextPacket(packetRecv);
		sendPacket(packetSent);
		byte[] data = new byte[packetSent.getPacketHeader().getDataSize()];
		
		int dataPosition = 0;
		while(dataPosition < packetSent.getPacketHeader().getDataSize()){
			packetRecv = recvPacket(packetSent);
			System.arraycopy(packetRecv.getData(), 0, data, dataPosition, packetRecv.getPacketHeader().getPacketSize());
			dataPosition += packetRecv.getPacketHeader().getPacketSize();
			packetSent = packetFactory.createClientRequestPacket(sourceIP, destIP, destPort, sourcePort, packetRecv.getPacketHeader().getDataSize(), dataPosition);
			sendPacket(packetSent);	
		}
		
		return data;
	}
	
	public int sendData(byte[] data) throws IOException, ClassNotFoundException{
		if(connectionState != 201) return -1;

		packetSent = packetFactory.createPutRequestPacket(sourceIP, destIP, destPort, sourcePort, data.length);
		sendPacket(packetSent);
		packetRecv = recvPacket(packetSent);
		if(packetRecv.getPacketHeader().getConnectionCode() != 501) return -1;		
		
		int dataPosition = 0;
		packetSent = packetFactory.createSendRequestPacket(sourceIP, destIP, destPort, sourcePort, data.length, 0, (512 - packetSent.getPacketHeader().getHeaderSize()) >= data.length ? data.length : 512 - packetSent.getPacketHeader().getHeaderSize());
				
		while(dataPosition < data.length){
			packetSent.setData(Arrays.copyOfRange(data, dataPosition, dataPosition + packetSent.getPacketHeader().getPacketSize()));
			sendPacket(packetSent);
			
			packetRecv = recvPacket(packetSent);
			dataPosition = packetRecv.getPacketHeader().getAckNumber();
			packetSent = packetFactory.createSendRequestPacket(sourceIP, destIP, destPort, sourcePort, data.length, packetRecv.getPacketHeader().getAckNumber(), (512 - packetSent.getPacketHeader().getHeaderSize()) >= data.length - dataPosition ? data.length - dataPosition : 512 - packetSent.getPacketHeader().getHeaderSize());
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
		serverSocket.send(sendPacket);
	}
	
	private RxPPacket recvPacket(RxPPacket lastPacketSent) throws IOException, ClassNotFoundException{
		byte[] recv = new byte[512];
		RxPPacket newRecvdPacket;
		recvPacket = new DatagramPacket(recv, recv.length);
		serverSocket.receive(recvPacket);
		newRecvdPacket = new RxPPacket();
		newRecvdPacket.setRxPPacketHeader(recv);
		//add data here if any? or perhaps somewhere else?
		if(newRecvdPacket.getPacketHeader().getDataSize() != 0 && newRecvdPacket.getPacketHeader().getPacketSize() != 0) {
			newRecvdPacket.setData(Arrays.copyOfRange(recv, newRecvdPacket.getPacketHeader().getHeaderSize(), newRecvdPacket.getPacketHeader().getHeaderSize() + newRecvdPacket.getPacketHeader().getPacketSize()));
		}
		return newRecvdPacket;
	}
	
	public void setWindow(int size){
		this.windowSize = size;
	}
	
	public void close(){
		if(!serverSocket.isClosed()){
			serverSocket.close();
			connectionState = 0;
		}
	}
}
