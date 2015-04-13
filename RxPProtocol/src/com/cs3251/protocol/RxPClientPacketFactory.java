package com.cs3251.protocol;

import java.util.Random;

import com.cs3251.RxPPacket;
import com.cs3251.RxPPacketHeader;

public class RxPClientPacketFactory {
//give state code and get bytes out;
	RxPPacket packet;
	RxPPacketHeader packetHeader;
	Random rand = new Random();
	
	public RxPClientPacketFactory(){}
	
	public RxPPacket createConnectionPacket(String sourceIP, String destIP, short destPort, short sourcePort) {
		packet = new RxPPacket(0, 0, Math.abs(rand.nextInt()), 0, sourceIP, destIP, destPort, sourcePort, 100);
		return packet;
	}
	
	public RxPPacket createPutRequestPacket(String sourceIP, String destIP, short destPort, short sourcePort, int dataSize){
		packet = new RxPPacket(0, dataSize, Math.abs(rand.nextInt()), 0, sourceIP, destIP, destPort, sourcePort, 500);
		return packet;
		
	}
	
	public RxPPacket createSendRequestPacket(String sourceIP, String destIP, short destPort, short sourcePort, int dataSize, int seqNumber, int packetSize){
		packet = new RxPPacket(packetSize, dataSize, seqNumber, 0, sourceIP, destIP, destPort, sourcePort, 506);
		return packet;
	}
	
	public RxPPacket createClientRequestPacket(String sourceIP, String destIP, short destPort, short sourcePort, int dataSize, int ackNumber){
		packet = new RxPPacket(0, dataSize, 0,ackNumber, sourceIP, destIP, destPort, sourcePort, 506);
		return packet;
	}
	
	public RxPPacket createGetRequestPacket(){
		return null;
	};
	
	public RxPPacket createNextPacket(RxPPacket packetRecvd) {
		packet = new RxPPacket();
		packetHeader = new RxPPacketHeader();
		switch(packetRecvd.getPacketHeader().getConnectionCode()){//201 == connected
			case 101:
				packetHeader.setDataSize(0);
				packetHeader.setPacketSize(0);
				packetHeader.setConnectionCode(200);
				packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
				packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
				packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
				packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
				packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
				packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
				break;
			case 501:
				packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
				packetHeader.setPacketSize(0);
				packetHeader.setConnectionCode(502);
				packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
				packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
				packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
				packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
				packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
				packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
				break;
				
			case 503:
				packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
				packetHeader.setPacketSize(0);
				packetHeader.setConnectionCode(505);
				packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
				packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
				packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
				packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
				packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
				packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
				break;
				
			case 500:
				packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
				packetHeader.setPacketSize(0);
				packetHeader.setConnectionCode(501);
				packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
				packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
				packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
				packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
				packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
				packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
				break;
				
			default:
				break;
		}
		packet.setRxPPacketHeader(packetHeader);
		return packet;
	}
	
	
}
