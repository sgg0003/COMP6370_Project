import java.security.SecureRandom;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

/*
 * Alice.java
 *
 * @author   Steven Grecu (sgg0003@auburn.edu)
 * @created  11/28/2018
 * @version  11/29/2018
 */


public class Alice {
  private static Alice alice;
  private DatagramSocket socket;
  private DatagramPacket packet;
  private InetAddress ip;
  private String message;
  private byte key[];
  private byte buf[];

  private final int RECV_PORT   = 9000;
  private final int SEND_PORT   = 8000;
  private final int BUFFER_SIZE = 65535;


  // Constructor sets up socket and sets IP address
  // to the local machine.
  private Alice() {
    this.key = new byte[8];
    try {
      this.socket = new DatagramSocket(RECV_PORT);
      this.ip = InetAddress.getLocalHost();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // There should only be one Alice object at a time.
  public static Alice getInstance() {
    if (alice == null)
      alice = new Alice();
    return alice;
  }         

  public void createKey() {
    SecureRandom rand = new SecureRandom();
    rand.nextBytes(key);
  }

  public void sendKey() throws IOException {
    packet = new DatagramPacket(key, key.length, ip, SEND_PORT); 
    socket.send(packet);
  }

  public void sendMsg() throws IOException {
    buf = message.getBytes();
    packet = new DatagramPacket(buf, buf.length, ip, SEND_PORT);
    socket.send(packet);
  }

  public void recvMsg() throws IOException {
    buf = new byte[BUFFER_SIZE];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);
    message = new String(packet.getData(), 0, packet.getLength());
  }

  public void encrypt() {
    message = Blowfish.encrypt(message, key);
  }

  public void decrypt() {
    message = Blowfish.decrypt(message, key);
  }

  public void verifySig() {
    String bobSig = "Signed: Bob (Seller)  Date: November 29, 2018.";
    int start = message.length() - bobSig.length();
    int end = message.length();
    if (message.substring(start, end).equals(bobSig))
      System.out.println("Alice:-The signature is good.");
    else
      System.out.println("Alice:-The signature is BAD!");
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
