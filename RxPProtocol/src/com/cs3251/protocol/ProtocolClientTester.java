package com.cs3251.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ProtocolClientTester {
	private static short clientPort;
	private static short netEmuPort;
	private static InetAddress ipAddress;
	
	public static void main(String args[]) throws ClassNotFoundException, IOException{
	//	short clientPort = 15888;
	//	short serverPort = 15889;
		
		if (args.length!=4||!args[0].equals("FxA-client")){
			System.out.println("The arguments entered were invalid. Exiting.");
			System.exit(0);
		}
		try{
			clientPort = Short.parseShort(args[1]);
		}
		catch(NumberFormatException ex){
			System.out.println("Port number must be an int. Exiting.");
			System.exit(0);
		}
		try{
			ipAddress = InetAddress.getByName(args[2]);
		}
		catch(UnknownHostException ex){
			System.out.println("IP Address was invalid. Exiting.");
			System.exit(0);
		}
		try{
			netEmuPort = Short.parseShort(args[3]);
		}
		catch(NumberFormatException ex){
			System.out.println("Port number was invalid. Exiting.");
			System.exit(0);
		}
		
		RxPClient client = new RxPClient("localhost", "localhost", clientPort, netEmuPort);
		
		//client.connect();
		Scanner scan = new Scanner(System.in);
		
		while(true){
			String nextLine = scan.nextLine();
			if (nextLine.length()<=10){
				String[] input = nextLine.split(" ");
				if (input.length==1){
					if(nextLine.equals("connect")){
						client.connect();
					}
					else if (nextLine.equals("disconnect")){
						client.close();
					}
					else{
						System.out.println("Invalid input.");
					}
					
				}
				else if (input.length==2){
					if (input[0].equals("get")){
						// call client get
					}
					else if(input[0].equals("post")){
						//extra if we have time
					}
					else if(input[0].equals("window")){
						// call client set window method
						//int size = Integer.parseInt(input[1]);
						//client.setWindow(size);
					}
					else{
						System.out.println("Invalid input.");
					}
				}
			}
			else{
				System.out.println("Invalid input.");
			}
		}
	}
}
