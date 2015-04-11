package com.cs3251;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RxPClient implements RxPClientInterface {
	private static int socketPort;
	private static InetAddress ipAddress;
	private static int portNum;
	
	
	public void startClientServer(){
		if ((socketPort&1)==1){
			System.out.println("Socket must bind to even port number. Exiting.");
			System.exit(0);
		}
	}
	public void connectToServer(){
		
	}
	public void get(){
		
	}
	public void post(){
		
	}
	public void setWindow(int windowSize){
		
	}
	public void disconnect(){
		
	}
	public void setTimeout(int timeout){
		
	}
	
	public static void main(String[] args){
		RxPClient c = new RxPClient();
		if (args.length!=4||!args[0].equals("FxA-client")){
			System.out.println("The arguments entered were invalid. Exiting.");
			System.exit(0);
		}
		try{
			socketPort = Integer.parseInt(args[1]);
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
			portNum = Integer.parseInt(args[3]);
		}
		catch(NumberFormatException ex){
			System.out.println("Port number was invalid. Exiting.");
			System.exit(0);
		}
		
		
		Scanner scan = new Scanner(System.in);
	}
}
