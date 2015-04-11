package com.cs3251;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RxPPacket {
	private RxPPacketHeader packetHeader;
	private byte[] data;
	
	public RxPPacket(){
		packetHeader = new RxPPacketHeader();
	};
	
	public RxPPacket(int packetSize, int dataSize, int seqNumber, int ackNumber, String sourceIP, String destIP, short destPort, short sourcePort, int connectionCode){
		packetHeader = new RxPPacketHeader(packetSize, dataSize, seqNumber, ackNumber, sourceIP, destIP, destPort, sourcePort, connectionCode);
	}
	
	public RxPPacketHeader getPacketHeader(){
		return packetHeader;
	}
	
	public void setRxPPacketHeader(byte[] data){
		packetHeader.byteToHeader(data);
	}
	
	public void setRxPPacketHeader(RxPPacketHeader header){
		packetHeader = header;
	}

	
	public String toString(){
		String strToRet = "";
		strToRet += "Packet Size: " + packetHeader.getPacketSize() + "\n";
		strToRet += "Data Size: " + packetHeader.getDataSize() + "\n";
		strToRet += "Sequence Number: " + packetHeader.getSeqNumber() + "\n";
		strToRet += "Ack Number: " + packetHeader.getAckNumber() + "\n";
		strToRet += "Connection Code: " + packetHeader.getConnectionCode() + "\n";
		strToRet += "Source IP: " + packetHeader.getSourceIP()+ ":" + packetHeader.getSourcePort() + "\n";
		strToRet += "Dest IP: " + packetHeader.getDestIP() + ":" + packetHeader.getDestPort() + "\n";
		
		return strToRet;
	}
	
	
}
