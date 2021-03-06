package com.cs3251.protocol;

import java.io.IOException;

public class NewClientTester {
	public static void main(String args[]) throws ClassNotFoundException, IOException {
		short clientPort = 15888;
		short serverPort = 15889;
		
		RxPClient client = new RxPClient("localhost", "localhost", clientPort, serverPort);
		
		client.connect();
		
		client.sendData("Sending Data...".getBytes());
		
		client.close();
	}
}
