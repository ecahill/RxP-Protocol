package com.cs3251.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ProtocolServerTester {
	private static short serverPort;
	private static short clientPort;
	private static InetAddress ipAddress;

	public static void main(String args[]) throws ClassNotFoundException, IOException{
		//short clientPort = 15888;
		//short serverPort = 15889;
		
		if (args.length!=4||!args[0].equals("FxA-server")){
			System.out.println("The arguments entered were invalid. Exiting.");
			System.exit(0);
		}
		try{
			serverPort = Short.parseShort(args[1]);
			if ((serverPort & 1)!=1){
				System.out.println("Server port number must be odd. Exiting.");
				System.exit(1);
			}
			clientPort = Short.parseShort(args[3]);
		}
		catch (NumberFormatException ex){
			System.out.println("The port number was invalid. Exiting.");
			System.exit(0);
		}
		try{
			ipAddress = InetAddress.getByName(args[2]);
		}
		catch (UnknownHostException ex){
			System.out.println("The IP Address was not specified correctly.");	
			System.exit(0);
		}
		
		RxPServer server = new RxPServer("localhost", "localhost", serverPort, clientPort);
		
		server.startRxPServer();
		
		Scanner scan = new Scanner(System.in);
		while(true){
			String nextLine = scan.nextLine();
			if (nextLine.length()>=8){
				String[] input = nextLine.split(" ");
				if (input[0].equals("window")){
					try{
						int size = Integer.parseInt(input[1]);
						server.setWindow(size);
					}
					catch(NumberFormatException ex){
						System.out.println("Invalid window size.");
					}
				}
				else{
					System.out.println("Invalid command.");
				}
			}
			if(nextLine.length()==9){
				if (nextLine.equals("terminate")){
					server.close();
				}
			}
			else{
				System.out.println("Invalid command.");
			}
		}
	}
}
