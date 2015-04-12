package com.cs3251;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RxPServer implements RxPServerInterface {
	private int windowSize;
	private int Timeout;
	private static int serverPort;
	private static int portNum;
	private static InetAddress ipAddress;
	private static DatagramSocket socket;

	public RxPServer(){
		
	}
	
	public void startServer(){
		if ((serverPort&1)!=1){
			System.out.println("The port number must be an odd number. Exiting.");
			System.exit(0);
		}
		try{
			socket = new DatagramSocket(serverPort);
		}
		catch (SocketException ex){
			System.out.println("Socket was not created. Exiting.");
			System.exit(0);
		}
		System.out.println("Socket was created on server port: "+serverPort+". Sending to client at: "+
		ipAddress+":"+portNum);
		
	}
	public void setWindow(int size){
		this.windowSize = size;
	}
	public void terminateServer(){
		System.out.println("Exiting system.");
		socket.close();
		System.exit(0);
	}
	public void setTimeout(int timeout){
		
	}
	
	public static void main(String[] args){
		RxPServer s = new RxPServer();
		if (args.length!=4||!args[0].equals("FxA-server")){
			System.out.println("The arguments entered were invalid. Exiting.");
			System.exit(0);
		}
		try{
			serverPort = Integer.parseInt(args[1]);
			portNum = Integer.parseInt(args[3]);
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
		s.startServer();	
		Scanner scan = new Scanner(System.in);
		while(true){
			String nextLine = scan.nextLine();
			if (nextLine.length()>=8){
				String[] input = nextLine.split(" ");
				if (input[0].equals("window")){
					try{
						int size = Integer.parseInt(input[1]);
						s.setWindow(size);
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
					s.terminateServer();
				}
			}
			else{
				System.out.println("Invalid command.");
			}
		}
	}
	
}
