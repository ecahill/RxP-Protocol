package com.cs3251;

import java.util.Scanner;

public class RxPClient implements RxPClientInterface {
	
	
	
	public void startClientServer(){
		//must bind to even number socket
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
		if (args.length!=4||!args[0].equals("FxA-client")){
			System.out.println("The arguments entered were invalid. Exiting.");
			System.exit(0);
		}
		
		
		Scanner scan = new Scanner(System.in);
	}
}
