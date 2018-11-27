import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.util.Scanner; 


public class Alice 
{ 
   private static int myPort  = 9000;
   private static int hisPort = 8000;
   private static final int BUFFER_SIZE = 65535;
   
   public static void main(String args[]) throws IOException
   {
      String line;
      Scanner scan = new Scanner(new File("contract.txt"));
      DatagramSocket ds = new DatagramSocket(myPort); // Socket for carrying data
   
      InetAddress ip = InetAddress.getLocalHost(); 
      byte buf[] = null;
   
      // Send the entire message of contract.txt
      while (scan.hasNextLine()) {
         line = scan.nextLine();
      
      	// convert the String input into the byte array. 
         buf = line.getBytes(); 
      
      	// Step 2 : Create the datagramPacket for sending 
      	// the data. 
         DatagramPacket DpSend = 
            new DatagramPacket(buf, buf.length, ip, hisPort); 
      
      	// Step 3 : invoke the send call to actually send 
      	// the data. 
         ds.send(DpSend);
      }
      scan.close();      
   
      // Listen for a message
      byte[] receive = new byte[BUFFER_SIZE];
      DatagramPacket DpReceive =
            new DatagramPacket(receive, receive.length);
      ds.receive(DpReceive);
      String newContract = udpBaseServer_2.data(receive).toString();
      System.out.println("Server:-" + newContract);

      // Copy received message to new file
      Charset charset = Charset.forName("US-ASCII");
      Path path = Paths.get("newContract.txt");
      try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
         writer.write(newContract);
      } catch (IOException e) {
         e.printStackTrace();
      }
      //Files.write(Paths.get("newContract.txt"), receive); //Bytes
      
      // Send a parting message
      buf = new String("bye").getBytes();
      //System.out.println("Server:-" + udpBaseServer_2.data(buf));
      DatagramPacket DpSend = 
            new DatagramPacket(buf, buf.length, ip, hisPort); 
      ds.send(DpSend);
   } 
} 
