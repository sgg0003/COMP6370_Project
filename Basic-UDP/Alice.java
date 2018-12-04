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


public class Alice { 
  private static DatagramSocket socket;
  private static InetAddress ip;
  private static byte buf[];
  private static Scanner scan;
  private static final int RECV_PORT   = 9000; // hard code ports
  private static final int SEND_PORT   = 8000;  // hard code ports
  private static final int BUFFER_SIZE = 65535;

  public static void main(String args[]) throws IOException {
    System.out.println("Preparing socket...\n");
    socket = new DatagramSocket(RECV_PORT); // Socket for carrying data
    ip = InetAddress.getLocalHost();  // Use local host ip addr 
    DatagramPacket packet;

    // Send the entire message of contract.txt
    scan = new Scanner(new File("contract.txt"));
    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      buf = line.getBytes(); 
      packet = new DatagramPacket(buf, buf.length, ip, SEND_PORT); 
      System.out.println("Sending a datagram...\n");
      socket.send(packet); // Send the contract
    }
    scan.close();

    // Listen for a message
    System.out.println("Listening...\n");
    buf = new byte[BUFFER_SIZE];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet); // Receive new contract
    String newContract =
      new String(packet.getData(), 0, packet.getLength());
    System.out.println("Received:-" + newContract + "\n");

    // Copy received message to new file
    Charset charset = Charset.forName("US-ASCII");
    Path path = Paths.get("newContract.txt");
    try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
      writer.write(newContract);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("EXITING...");
  } 
} 
