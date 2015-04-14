package com.cs3251.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Scanner;

public class ProtocolServerTester {
	private static short serverPort;
	private static short netEmuPort;
	private static String ipAddress;

	public static void main(String args[]) throws ClassNotFoundException, IOException{
		
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
			netEmuPort = Short.parseShort(args[3]);
		}
		catch (NumberFormatException ex){
			System.out.println("The port number was invalid. Exiting.");
			System.exit(0);
		}
		ipAddress = args[2];
		
		RxPServer server = new RxPServer("localhost", ipAddress, serverPort, netEmuPort);
		
		server.startRxPServer();
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader scan = new BufferedReader(in);
		Scanner s = new Scanner(System.in);
		boolean run = true;
		boolean flag = true;
		String nextLine = "";
		byte[] request = null;
		while(run){
		//	request = null;
		//	if (flag){
				request = server.runServer();
			//}
			
			if (request!= null){
				//System.out.println("Receiving request from client.");
				String val = new String(request);
				if (val.indexOf("GET*")!=-1){
					String fRqst = val.substring(4);
					fRqst = System.getProperty("user.dir")+"\\"+ fRqst;
					System.out.println("Searching for filepath: "+fRqst);
					File f = new File(fRqst);
					if (f.exists()){
						byte[] fileIn = new byte[Files.readAllBytes(f.toPath()).length];
						fileIn = Files.readAllBytes(f.toPath());
						server.sendData(fileIn);
					}
					else{
						System.out.println("The file "+fRqst+" was not found.");
						server.sendData(new byte[0]);
					}
				}
				else if (val.indexOf("POST*")!=-1){
					String fname = val.substring(5);
					byte[] serverResponse = "!".getBytes();
					System.out.println("Sending ready response.");
					int response = server.sendData(serverResponse);
					if (response>=0){
						byte[] clientResponse = null;
						System.out.println("Waiting for file.");
						do{
							clientResponse = server.runServer();
						
						}
						while(clientResponse == null);
						
						if (clientResponse.length != 0){
							System.out.println("Post was sucessful.");
							FileOutputStream fos = new FileOutputStream(fname);
							fos.write(clientResponse);
							fos.close();
						}
						else{
							System.out.println("Post was unsucessful.");
						}
					}
					else{
						System.out.println("Unable to send resposne.");
					}
				}

				else{
					System.out.println("Invalid Request.");
					server.sendData(new byte[0]);
				}
			
			}
			else{
				//System.out.println("is scanner ready?");
			if (System.in.available()>0){
				System.out.println("Waiting for input: ");
				nextLine = scan.readLine();
				//nextLine = s.nextLine();
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
					else if (input[0].equals("terminate")){
						server.close();
						run = false;
					}
					else{
						System.out.println("Invalid command.");
					}
				}
				else{
					System.out.println("Invalid command.");
				}
			}
			}
		}
	}
}
