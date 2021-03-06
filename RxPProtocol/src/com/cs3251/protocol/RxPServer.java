package com.cs3251.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.zip.CRC32;

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
	private CRC32 crc;
	private int windowSize;
	
	private RxPPacket packetSent;
	private RxPPacket packetRecv;
	
	public RxPServer(String sourceIP, String destIP, short sourcePort, short destPort){
		this.sourceIP = sourceIP;
		this.sourcePort = sourcePort;
		this.destIP = destIP;
		this.destPort = destPort;
		this.connectionState = 0;
		crc = new CRC32();
		windowSize = 1;
		packetFactory = new RxPServerPacketFactory();
	}
	//initalizes the server and starts listening for a connection request
	public int startRxPServer() throws IOException, ClassNotFoundException{
		serverSocket = new DatagramSocket(sourcePort);
		packetSent = packetFactory.createConnectionPacket(sourceIP, destIP, destPort, sourcePort);
		System.out.println("Awaiting connection...");
		packetRecv = recvPacket(packetSent);
		packetSent = packetFactory.createNextPacket(packetRecv, sourceIP, sourcePort);
		sendPacket(packetSent);
		packetRecv = recvPacket(packetSent);
		if(packetRecv.getPacketHeader().getAckNumber() != packetSent.getPacketHeader().getSeqNumber() + 1) return -1;
		packetSent = packetFactory.createNextPacket(packetRecv, sourceIP, sourcePort);
		sendPacket(packetSent);
		connectionState = packetSent.getPacketHeader().getConnectionCode();
		return connectionState;
	}	
	//once a connection has been made, this listens for data get/put requests
	public byte[] runServer() throws ClassNotFoundException, IOException{
		if(connectionState != 201) return null;
		packetRecv = recvPacket(packetSent);
		if(packetRecv.getPacketHeader().getConnectionCode() == 500){
			return clientSendRequestHandler();
		}
		if(packetRecv.getPacketHeader().getConnectionCode() == 400){
			return clientSendRequestHandler();
		}
		
		return null;
	}
	//used to get the data from a client put request
	private byte[] clientSendRequestHandler() throws IOException, ClassNotFoundException{
		packetSent = packetFactory.createNextPacket(packetRecv, sourceIP, sourcePort);
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
	//used to send the data to a client from a get request
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
	//method used to make byte[] from packets to be sent over the connection
	private void sendPacket(RxPPacket packetToSend) throws IOException{
		byte[] bytesToSend = new byte[512];
		byte[] packetHeader = packetToSend.getPacketHeader().headerToByte();
		System.arraycopy(packetHeader, 0, bytesToSend, 0, packetHeader.length);
		if(packetToSend.getPacketHeader().getPacketSize() != 0){
			System.arraycopy(packetToSend.getData(), 0, bytesToSend, packetHeader.length, packetToSend.getPacketHeader().getPacketSize());
		}
		crc.update(Arrays.copyOfRange(bytesToSend, 4, packetHeader.length + packetToSend.getPacketHeader().getPacketSize() - 4));
		int checksum = (int)crc.getValue();
		bytesToSend[0] = (byte) ((checksum >> 24) & 0xff);
		bytesToSend[1] = (byte) ((checksum >> 16) & 0xff);
		bytesToSend[2] = (byte) ((checksum >> 8) & 0xff);
		bytesToSend[3] = (byte) (checksum & 0xff);
		sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(packetToSend.getPacketHeader().getDestIP()), packetToSend.getPacketHeader().getDestPort());
		serverSocket.send(sendPacket);
	}
	//used to turn byte[] into packets
	private RxPPacket recvPacket(RxPPacket lastPacketSent) throws IOException, ClassNotFoundException{
		byte[] recv = new byte[512];
		RxPPacket newRecvdPacket;
		recvPacket = new DatagramPacket(recv, recv.length);
		serverSocket.receive(recvPacket);
		newRecvdPacket = new RxPPacket();
		newRecvdPacket.setRxPPacketHeader(recv);
		if(newRecvdPacket.getPacketHeader().getDataSize() != 0 && newRecvdPacket.getPacketHeader().getPacketSize() != 0) {
			newRecvdPacket.setData(Arrays.copyOfRange(recv, newRecvdPacket.getPacketHeader().getHeaderSize(), newRecvdPacket.getPacketHeader().getHeaderSize() + newRecvdPacket.getPacketHeader().getPacketSize()));
		}
		crc.update(Arrays.copyOfRange(recv, 4, newRecvdPacket.getPacketHeader().getHeaderSize() + newRecvdPacket.getPacketHeader().getPacketSize() - 4));
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
