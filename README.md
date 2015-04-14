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
<ul>
<li><b> RxPClient(String sourceIP, String destIP, short sourceDest, short destPort)</b></li>
-This constructor is call by a host to create a client RxP instance. The method tries to initializes and returns the RxPClient object.

<li><b>RxPClient.connect()</b></li>
-This method is called by the RxP client instance to try and connect to the RxP server instance at destIP:destPort.

<li><b>int RxPClient.sendData( byte data[])</b></li>
-This method will try to send an array of bytes from the data buffer over the connection made by the RxP client and server.  If successful, it will return 0, otherwise it will return with an error code.

<li><b>Byte[] RxPClient.getData(byte data[])</b></li>
-This method will request to receive data from the RxP server instance. If successful the data will be returned in a byte array.

<li><b>void  RxPClient.close()</b></li>
-This method will send a close request to the RxP server instance and close itself.

<li><b>RxPServer(String sourceIP, String destIP, short sourceDest, short destPort)</b></li>
-This constructor is call by a host to create a server RxP instance. The method tries to initializes and returns the RxPServer object.

<li><b>RxPServer.startRxPServer()</b></li>
-This method is called by the RxP server instance to bind to sourcePort and wait to receive a connection.

<li><b>int RxPServer.runServer( byte data[])</b></li>
-once a connection is established, this method is called to listed to requests from the RxP client instance.

<li><b>int RxPServer.sendData(byte data[])</b></li>
-Upon a getData request from the RxP client instance, this method send the requested data to the client.

<li><b>void  RxPServer.close()</b></li>
-This method will send a close request to the RxP client instance and close itself.

</ul>
</p>

<p><b>Bugs and Limitations: </b>
During the development process, there were issues involving checksum that hindered the ability to check for packet validity. Without this in place we will be handling packets that could be corrupted and not know it.  An improvement on this would be to work out the kinks with tranfering the checksum into bytes we ran into.  Another Improvement we could make is adding the ability to have sliding window functionality.  This would enable higher throughput and less overhead!
</p>
