package com.cs3251.protocol;

import java.util.Random;

import com.cs3251.RxPPacket;
import com.cs3251.RxPPacketHeader;

public class RxPServerPacketFactory {
	RxPPacket packet;
	RxPPacketHeader packetHeader;
	Random rand = new Random();
	
	public RxPServerPacketFactory(){}
	
	public RxPPacket createConnectionPacket(String sourceIP, String destIP, short destPort, short sourcePort) {
		packet = new RxPPacket(0, 0, 0, 0, sourceIP, destIP, destPort, sourcePort, 0);
		packet.setChecksum(-1);
		return packet;
	}
	
	public RxPPacket createClientRequestPacket(String sourceIP, String destIP, short destPort, short sourcePort, int dataSize, int ackNumber){
		packet = new RxPPacket(0, dataSize, 0,ackNumber, sourceIP, destIP, destPort, sourcePort, 506);
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
	
	public RxPPacket createNextPacket(RxPPacket packetRecvd, String sourceIP, short sourcePort){
		packet = new RxPPacket();
		packetHeader = new RxPPacketHeader();
		switch(packetRecvd.getPacketHeader().getConnectionCode()){//201 == connected
		case 100:
			//start connection
			packetHeader.setDataSize(0);
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(101);
			packetHeader.setSourceIP(sourceIP);
			packetHeader.setSourcePort(sourcePort);
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getDestPort());
			//packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			//packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
			packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
			break;
		case 200:
			packetHeader.setDataSize(0);
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(201);
			packetHeader.setSourceIP(sourceIP);
			packetHeader.setSourcePort(sourcePort);
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getDestPort());
			//packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			//packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
			packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
			break;
			
		case 500:
			packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(501);
			packetHeader.setSourceIP(sourceIP);
			packetHeader.setSourcePort(sourcePort);
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getDestPort());
			//packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			//packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
			packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
			break;
			
		case 502:
			packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(503);
			packetHeader.setSourceIP(sourceIP);
			packetHeader.setSourcePort(sourcePort);
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getDestPort());
			//packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			//packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
			packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
			break;
			
		case 505:
			packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(506);
			packetHeader.setSourceIP(sourceIP);
			packetHeader.setSourcePort(sourcePort);
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getDestPort());
			//packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			//packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
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
