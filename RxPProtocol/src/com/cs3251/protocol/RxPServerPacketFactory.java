package com.cs3251.protocol;

import java.net.InetAddress;
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
		return packet;
	}
	
	public RxPPacket createNextPacket(RxPPacket packetRecvd){
		packet = new RxPPacket();
		packetHeader = new RxPPacketHeader();
		switch(packetRecvd.getPacketHeader().getConnectionCode()){//201 == connected
		case 100:
			//start connection
			packetHeader.setDataSize(0);
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(101);
			packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
			packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
			packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
			break;
		case 200:
			packetHeader.setDataSize(0);
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(201);
			packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
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
			
		case 502:
			packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(503);
			packetHeader.setSourceIP(packetRecvd.getPacketHeader().getDestIP());
			packetHeader.setDestIP(packetRecvd.getPacketHeader().getSourceIP());
			packetHeader.setDestPort(packetRecvd.getPacketHeader().getSourcePort());
			packetHeader.setSourcePort(packetRecvd.getPacketHeader().getDestPort());
			packetHeader.setSeqNumber(Math.abs(rand.nextInt()));
			packetHeader.setAckNumber(packetRecvd.getPacketHeader().getSeqNumber() + 1);
			break;
			
		case 505:
			packetHeader.setDataSize(packetRecvd.getPacketHeader().getDataSize());
			packetHeader.setPacketSize(0);
			packetHeader.setConnectionCode(506);
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
