package com.cs3251.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ProtocolServerTester {

	public static void main(String args[]) throws ClassNotFoundException, IOException{
		short clientPort = 15888;
		short serverPort = 15889;
		
		RxPServer server = new RxPServer("localhost", "localhost", serverPort, clientPort);
		
		server.startRxPServer();

		server.close();
		
	}
}
