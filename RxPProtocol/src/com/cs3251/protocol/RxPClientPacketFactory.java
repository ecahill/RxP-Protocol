package com.cs3251.protocol;

import java.net.InetAddress;
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
				
			default:
				break;
		}
		packet.setRxPPacketHeader(packetHeader);
		return packet;
	}
	
	
}
