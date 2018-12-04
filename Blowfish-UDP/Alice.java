import java.security.SecureRandom;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;


/*
 * Alice.java
 *
 * @author   Steven Grecu (sgg0003@auburn.edu)
 * @created  11/28/2018
 * @version  11/29/2018
 */


public class Alice {
   private static DatagramSocket socket;
   private static DatagramPacket packet;
   private static InetAddress ip;
   private static String message;
   private static byte key[];
   private static byte buf[];
   private static Blowfish bf;
   private final static int RECV_PORT   = 9000;
   private final static int SEND_PORT   = 8000;
   private final static int BUFFER_SIZE = 65535;

   // Reads the contract file into message;
   private static void readContract() {
      String line = "";
      try {
         System.out.println("Reading contract...\n");
         Scanner scan = new Scanner(new File("contract.txt"));
         while (scan.hasNextLine())
            line += scan.nextLine();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      message = line;
   }

   // Copy received message to new file
   private static void writeContract(String contract) {
      Charset charset = Charset.forName("ISO-8859-1");
      Path path = Paths.get("signed_contract.txt");
      try (BufferedWriter writer =
           Files.newBufferedWriter(path, charset)) {
         System.out.println(
              "Writing signed contract to \"signed_contract.txt\".\n");
         writer.write(contract);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   // Create the key and initialize the Blowfish algorithm.
   public static void createKey() {
      key = new byte[8];
      try {
         System.out.println("Creating session key...\n");
         SecureRandom rand = new SecureRandom();
         rand.nextBytes(key);
      } catch (Exception e) {
         e.printStackTrace();
      }
      bf = new Blowfish(key);
   }

   public static void sendKey() throws IOException {
      packet = new DatagramPacket(key, key.length, ip, SEND_PORT); 
      System.out.println("Sending the key...\n");
      socket.send(packet);
   }

   public static void sendMsg() throws IOException {
      buf = message.getBytes("ISO-8859-1");
      packet = new DatagramPacket(buf, buf.length, ip, SEND_PORT);
      System.out.println("Sending a datagram...\n");
      socket.send(packet);
   }

   public static void recvMsg() throws IOException {
      buf = new byte[BUFFER_SIZE];
      packet = new DatagramPacket(buf, buf.length);
      System.out.println("Listening...\n");
      socket.receive(packet);
      message = new String(packet.getData(), 0, packet.getLength(), "ISO-8859-1");
      System.out.println("Received: " + message + "\n");
   }

   public static void encrypt() throws Exception {
      System.out.println("Encrypting...\n");
      message = bf.encrypt(message, key);
   }

   public static void decrypt() throws Exception {
      System.out.println("Decrypting...\n");
      message = bf.decrypt(message, key);
   }

   public static void verifySig() {
      String bobSig = "Signed: Bob (Seller)  Date: November 29, 2018.";
      int start = message.length() - bobSig.length();
      int end = message.length();
      System.out.print("Checking signature... ");
      if (message.substring(start, end).equals(bobSig))
         System.out.println("The signature is good.\n");
      else
         System.out.println("The signature is BAD!\n");
   }

   // This is where the magic happens.
   public static void main(String[] args) throws Exception {
     // Create the socket
     System.out.println("Creating a socket...\n");
     socket = new DatagramSocket(RECV_PORT);
     ip = InetAddress.getLocalHost();
     createKey();
     sendKey();
     recvMsg(); // Acknowledgement of key reception
     readContract();
     encrypt();
     sendMsg();
     recvMsg();
     decrypt();
     verifySig();
     writeContract(message);
     System.out.println("EXITING.\n");
   }
}
