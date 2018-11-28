import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 


public class Bob 
{
   private static DatagramSocket socket;
   private static DatagramPacket packet;
   private static InetAddress ip;
   private static byte buf[];
   private static int myPort  = 8000;
   private static int hisPort = 9000;
   private static final int BUFFER_SIZE = 65535;
   private static final String MESSAGE =
         "  Signed: Bob (Seller)  Date: November 29, 2018.";

   
   public static void main(String[] args) throws IOException 
   { 
   	// Step 1 : Create a socket to listen at my port 
      socket = new DatagramSocket(myPort); 
      buf = new byte[BUFFER_SIZE];
      ip = InetAddress.getLocalHost();  // Using local machine
   
   	//while (true) { 
      // Step 2 : create a DatgramPacket to receive the data. 
      packet = new DatagramPacket(buf, buf.length); 
      // Step 3 : revieve the data in byte buffer. 
      socket.receive(packet);
      String received =
         new String(packet.getData(), 0, packet.getLength());
      System.out.println("\nClient:-" + received);
      buf = new byte[BUFFER_SIZE];    // Clean the buffer
      
      // Send a bit of data
      String msg = received + MESSAGE;
      System.out.println("\nNew Message:-" + msg);
      buf = msg.getBytes();
      packet = new DatagramPacket(buf, buf.length, ip, hisPort);
      socket.send(packet);
   
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);
      // Exit the server if the client sensocket "bye"
      received = new String(packet.getData(), 0, packet.getLength()); 
      if (received.equals("bye")) { 
         System.out.println("\nClient sent bye.....EXITING");
      } else {
         System.out.println("\nClient:-" + received);
      }
   } 

} 
