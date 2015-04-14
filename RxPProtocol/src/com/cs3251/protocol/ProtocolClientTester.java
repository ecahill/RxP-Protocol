package com.cs3251.protocol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class ProtocolClientTester {
	private static short clientPort;
	private static short netEmuPort;
	private static String ipAddress;
	
	public static void main(String args[]) throws ClassNotFoundException, IOException{
		
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
		ipAddress = args[2];

		try{
			netEmuPort = Short.parseShort(args[3]);
		}
		catch(NumberFormatException ex){
			System.out.println("Port number was invalid. Exiting.");
			System.exit(0);
		}
		
		RxPClient client = new RxPClient("localhost", ipAddress, clientPort, netEmuPort);
		
		Scanner scan = new Scanner(System.in);
		boolean run = true;
		while(run){
			String nextLine = scan.nextLine();
			String[] input = nextLine.split(" ");
			if (input.length==1){
				if(nextLine.equals("connect")){
					client.connect();
				}
				else if (nextLine.equals("disconnect")){
					client.close();
					scan.close();
					run = false;
				}
				else{
					System.out.println("Invalid input.");
				}
			}		
			else if (input.length==2){
				if (input[0].equals("get")){
					String filename = input[1];
					System.out.println("Looking for file: "+filename);
					String request = "GET*"+filename;
					byte[] ret = client.getData(request.getBytes());
					if (ret.length!=0){
						System.out.println("Writing file: "+filename);
						FileOutputStream fos = new FileOutputStream(filename);
						fos.write(ret);
						fos.close();
						System.out.println("Successful GET.");
					}
					else{
						System.out.println("Unsuccessful GET.");
					}
				}
				else if(input[0].equals("post")){
					String filename = input[1];
					byte[] rqst = client.getData(("POST*"+filename).getBytes());
					String r = new String(rqst);
					System.out.println("Sent post notice.");
					if (r.equals("!")){
						String fRqst = System.getProperty("user.dir")+"\\"+filename;
						System.out.println("Searching for filepath: "+fRqst);
						File f = new File(fRqst);
						if (f.exists()){
							byte[] fileIn = new byte[Files.readAllBytes(f.toPath()).length];
							fileIn = Files.readAllBytes(f.toPath());
							client.sendData(fileIn);
						}
						else{
							System.out.println("File was not found.");
							client.sendData(new byte[0]);
						}
					}
					else{		
						System.out.println("Incorrect request received.");
					}
				}
				else{
					System.out.println("Invalid input.");
					
				}
			}
		}
	}
}
