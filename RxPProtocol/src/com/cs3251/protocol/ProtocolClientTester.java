package com.cs3251.protocol;

import java.io.IOException;
import java.net.InetAddress;

public class ProtocolClientTester {
	public static void main(String args[]) throws ClassNotFoundException, IOException{
		short clientPort = 15888;
		short serverPort = 15889;
		
		RxPClient client = new RxPClient("localhost", "localhost", clientPort, serverPort);
		
		client.connect();
		
		client.close();
		
	}
}
