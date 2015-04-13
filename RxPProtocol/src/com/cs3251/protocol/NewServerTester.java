package com.cs3251.protocol;

import java.io.IOException;

public class NewServerTester {
	public static void main(String args[]) throws ClassNotFoundException, IOException {
		short clientPort = 15888;
		short serverPort = 15889;
		
		RxPServer server = new RxPServer("localhost", "localhost", serverPort, clientPort);
		
		server.startRxPServer();
		
		System.out.println(new String(server.runServer()));
		
		server.sendData("Is this the data you want?".getBytes());
		
		server.close();
	}
	
}
