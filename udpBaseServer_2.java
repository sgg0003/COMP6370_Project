// Java program to illustrate Server side 
// Implementation using DatagramSocket 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 

//import networking.udpBaseClient; 

public class udpBaseServer_2 
{
   private static int myPort  = 8000;
   private static int hisPort = 9000;
   private static final int BUFFER_SIZE = 65535;
   
	// A utility method to convert the byte array 
	// data into a string representation. 
   public static StringBuilder data(byte[] a) 
   { 
      if (a == null) 
         return null; 
      StringBuilder ret = new StringBuilder(); 
      int i = 0; 
      while (a[i] != 0) 
      { 
         ret.append((char) a[i]); 
         i++; 
      } 
      return ret; 
   }

   
   public static void main(String[] args) throws IOException 
   { 
   	// Step 1 : Create a socket to listen at my port 
      DatagramSocket ds = new DatagramSocket(myPort); 
      byte[] receive = new byte[BUFFER_SIZE];
      DatagramPacket DpReceive = null; 
      InetAddress ip = InetAddress.getLocalHost();
   
   	//while (true) { 
      // Step 2 : create a DatgramPacket to receive the data. 
      DpReceive = new DatagramPacket(receive, receive.length); 
      // Step 3 : revieve the data in byte buffer. 
      ds.receive(DpReceive); 
      System.out.println("Client:-" + data(receive));
      receive = new byte[65535];
      
      // Send a bit of data
      byte buf[] = "Oh, shoot!".getBytes();
      ds.send(new DatagramPacket(buf, buf.length, ip, hisPort));
   
      DpReceive = new DatagramPacket(receive, receive.length);
      ds.receive(DpReceive);
      // Exit the server if the client sends "bye" 
      if (data(receive).toString().equals("bye")) { 
         System.out.println("Client sent bye.....EXITING");
      } else {
         System.out.println("Client:-" + data(receive));
      }
      //}
   } 

} 
