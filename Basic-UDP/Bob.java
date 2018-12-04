import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 


public class Bob {
  private static DatagramSocket socket;
  private static DatagramPacket packet;
  private static InetAddress ip;
  private static byte buf[];
  private static int myPort  = 8000;
  private static int hisPort = 9000;
  private static final int BUFFER_SIZE = 65535;
  private static final String MESSAGE =
    "  Signed: Bob (Seller)  Date: November 29, 2018.";

  public static void main(String[] args) throws IOException { 
    System.out.println("Creating a socket...\n");
    socket = new DatagramSocket(myPort); 
    buf = new byte[BUFFER_SIZE];
    ip = InetAddress.getLocalHost();  // Using local machine
    packet = new DatagramPacket(buf, buf.length); 

    // Listen for datagram
    System.out.println("Listening...\n");
    socket.receive(packet);
    String received =
      new String(packet.getData(), 0, packet.getLength());
    System.out.println("Received:-" + received + "\n");
    //buf = new byte[BUFFER_SIZE];    // Clean the buffer

    // Send a bit of data
    String msg = received + MESSAGE; // Append signature
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, ip, hisPort);
    socket.send(packet);
    System.out.println("Sending message...\n");

    System.out.println("EXITING...");
  } 
} 
