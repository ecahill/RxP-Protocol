# RxP-Protocol

<p><b>Team members:</b> Emily Cahill (ecahill3@gatech.edu) and Brandon Foley (bfoley13@gatech.edu)</p>
<p><b>Class</b>: CS 3251, Computer Networks, Section B</p>
<p><b>Date</b>: 04/13/2015</p>
<p><b>Assignment</b>: Project 2, RxP Protocol</p>

<p><b>Files Submitted</b>:
<ul>
<li>RxPServer.java</li>
- This class represents the server functionality of the RxP Protocol
<li>RxPClient.java</li>
- This class represents the client functionality of the RxP Protocol
<li>RxPServerPacketFactory.java</li>
- This class is used to create new packets or next packets that depend on the connection code of the packet
<li>RxPClientPacketFactory.java</li>
- This class is used to create new packets, request packets, or next packets that depend on the connection code of the packet. 
<li>RxPPacket.java</li>
- The class represents a packet with a packet header and data
<li>RxPPacketHeader.java</li>
- This class contains all header information of each RxPPacket
<li>ProtocolServerTester.java</li>
- A runnable class that represents the server of the file transfer applicaiton
<li>ProtocolClientTester.java</li>
- A runnable class that represents the client of the file transfer application
<li>FxA-server.jar</li>
- A jar file that runs the server of the file transfer application
<li>FxA-client.jar</li>
- A jar file runs the client of the file transfer application
<li>Sample.txt</li>
- File of sample program output
</ul>
</p>

<p><b>Compilation Instructions:</b>
Open up a terminal and run the NetEmu. Open up another terminal and navigate to the location of FxA-server.jar. Open up another terminal and navigate to the location of FxA-client.jar. In the first terminal, type: "java -jar FxA-server.jar FxA-server <port number of application server> <IP Address> <port number of NetEmu>". In the second terminal, type: "java -jar FxA-client.jar FxA-client <port number of application client> <IP Address> <port number of NetEmu>". To connect the client to the server, type "connect" in the client's terminal. To get a file from the server, type: "get <filename>". To post a file to the server, type: "post <filename>". To disconnect the client from the server, type: "disconnect". 
</p>

<p><b>Updated Protocol: </b>

</p>

<p><b>Bugs and Limitations: </b>

</p>
