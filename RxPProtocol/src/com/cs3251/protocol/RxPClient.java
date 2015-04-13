package com.cs3251.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.zip.CRC32;

import com.cs3251.RxPPacket;

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
	private CRC32 crc;
	private int windowSize;

	private RxPPacket packetSent;
	private RxPPacket packetRecv;
	
	public RxPClient(String sourceIP, String destIP, short sourcePort, short destPort){
		this.sourceIP = sourceIP;
		this.sourcePort = sourcePort;
		this.destIP = destIP;
		this.destPort = destPort;
		this.connectionState = 0;
		crc = new CRC32();
		windowSize = 1;
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
		packetSent = packetFactory.createSendRequestPacket(sourceIP, destIP, destPort, sourcePort, data.length, 0, (512 - packetSent.getPacketHeader().getHeaderSize()) >= data.length ? data.length : 512 - packetSent.getPacketHeader().getHeaderSize());
				
		while(dataPosition < data.length){
			packetSent.setData(Arrays.copyOfRange(data, dataPosition, dataPosition + packetSent.getPacketHeader().getPacketSize()));
			sendPacket(packetSent);
			
			packetRecv = recvPacket(packetSent);
			dataPosition = packetRecv.getPacketHeader().getAckNumber();
			packetSent = packetFactory.createSendRequestPacket(sourceIP, destIP, destPort, sourcePort, data.length, packetRecv.getPacketHeader().getAckNumber(), (512 - packetSent.getPacketHeader().getHeaderSize()) >= data.length -  dataPosition ? data.length - dataPosition : 512 - packetSent.getPacketHeader().getHeaderSize());
		}
		return 0;
	}
	
	public byte[] getData(byte[] data) throws ClassNotFoundException, IOException{
		sendData(data);
		packetRecv = recvPacket(packetSent);
		
		return serverSendRequestHandler();
		
	}
	
	private byte[] serverSendRequestHandler() throws IOException, ClassNotFoundException{
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
	
	private void sendPacket(RxPPacket packetToSend) throws IOException{
		byte[] bytesToSend = new byte[512];
		byte[] packetHeader = packetToSend.getPacketHeader().headerToByte();
		System.arraycopy(packetHeader, 0, bytesToSend, 0, packetHeader.length);
		if(packetToSend.getPacketHeader().getPacketSize() != 0){
			System.arraycopy(packetToSend.getData(), 0, bytesToSend, packetHeader.length, packetToSend.getPacketHeader().getPacketSize());
		}
		crc.update(Arrays.copyOfRange(bytesToSend, 4, packetHeader.length + packetToSend.getPacketHeader().getPacketSize() - 4));
		int checksum = (int)crc.getValue();
		System.out.println(checksum);
		bytesToSend[0] = (byte) ((checksum >> 24) & 0xff);
		bytesToSend[1] = (byte) ((checksum >> 16) & 0xff);
		bytesToSend[2] = (byte) ((checksum >> 8) & 0xff);
		bytesToSend[3] = (byte) (checksum & 0xff);
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
		if(newRecvdPacket.getPacketHeader().getDataSize() != 0 && newRecvdPacket.getPacketHeader().getPacketSize() != 0) {
			newRecvdPacket.setData(Arrays.copyOfRange(recv, newRecvdPacket.getPacketHeader().getHeaderSize(), newRecvdPacket.getPacketHeader().getHeaderSize() + newRecvdPacket.getPacketHeader().getPacketSize()));
		}
		crc.update(Arrays.copyOfRange(recv, 4, newRecvdPacket.getPacketHeader().getHeaderSize() + newRecvdPacket.getPacketHeader().getPacketSize() - 4));
		int checksum = (int)crc.getValue();
		System.out.println(checksum);
		if(checksum == newRecvdPacket.getPacketHeader().getChecksum())
			System.out.println("match!");
		return newRecvdPacket;
	}
	
	public void close() {
		if(!clientSocket.isClosed()){
			clientSocket.close();
			connectionState = 0;
		}
		
	}
	
}
