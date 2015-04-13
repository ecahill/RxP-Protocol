package com.cs3251.protocol;

import java.io.IOException;
import java.lang.reflect.Array;
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
			
		}
		
		return data;
		
		
	}
	
	private void sendPacket(RxPPacket packetToSend) throws IOException{
		byte[] bytesToSend = new byte[512];
		byte[] packetHeader = packetToSend.getPacketHeader().headerToByte();
		System.arraycopy(packetHeader, 0, bytesToSend, 0, packetHeader.length);
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
