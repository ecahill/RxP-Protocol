package com.cs3251;

public class RxPPacket {
	//Packet sent over RxP connection
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
	
	public void setData(byte[] data){
		this.data = data;
	}
	
	public byte[] getData(){
		return data;
	}
	
	public void setChecksum(int checksum){
		packetHeader.setChecksum(checksum);
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
