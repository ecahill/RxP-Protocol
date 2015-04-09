package com.cs3251;

public class RxPPacket {

	private int packetSize;//512 bytes
	private int seqNumber;
	private int ackNumber;
	private short destPort;
	private short sourcePort;
	private int checksum;
	private int connectionCode;
	private byte[] data;
	

}
