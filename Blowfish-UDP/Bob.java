import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

/*
 * Bob.java
 *
 * @author   Steven Grecu (sgg0003@auburn.edu)
 * @created  11/28/2018
 * @version  11/29/2018
 */


public class Bob {
  private static DatagramSocket socket;
  private static DatagramPacket packet;
  private static InetAddress ip;
  private static String message;
  private static byte[] key;
  private static byte[] buf;
  private static Blowfish bf;
  private static final int RECV_PORT   = 8000;
  private static final int SEND_PORT   = 9000;
  private static final int BUFFER_SIZE = 65535;
  private static final String SIGNATURE =
    "Signed: Bob (Seller)  Date: November 29, 2018.";

  public static void recvKey() throws IOException {
    buf = new byte[BUFFER_SIZE];
    packet = new DatagramPacket(buf, buf.length);
    System.out.println("Listening...\n");
    socket.receive(packet);
    for (int i = 0; i < key.length; i++)
      key[i] = packet.getData()[i];
    System.out.println("Received: " + new String(key) + "\n");
    bf = new Blowfish(key);
  }

  public static void sendKeyConfirm() throws IOException {
    message = "I (Bob) have received the key.";
    sendMsg();
  }

  public static void recvMsg() throws IOException {
    buf = new byte[BUFFER_SIZE];
    packet = new DatagramPacket(buf, buf.length);
    System.out.println("Listening...\n");
    socket.receive(packet);
    message = new String(packet.getData(), 0, packet.getLength(), "ISO-8859-1");
    System.out.println("Received: " + message + "\n");
  }

  public static void sendMsg() throws IOException {
    buf = message.getBytes("ISO-8859-1");
    packet = new DatagramPacket(buf, buf.length, ip, SEND_PORT);
    System.out.println("Sending datagram...\n");
    socket.send(packet);
  }

  public static void encrypt() throws Exception {
    System.out.println("Encrypting...\n");
    message = bf.encrypt(message, key);
  }

  public static void decrypt() throws Exception {
    System.out.println("Decrypting...\n");
    message = bf.decrypt(message, key);
  }

  public static void sign() {
    System.out.println("Signing the contract...\n");
    message = message + "  " + SIGNATURE;
  }

  // This is where the magic happens.
  public static void main(String[] args) throws Exception {
    key = new byte[8];
    System.out.println("Creating a socket...\n");
    socket = new DatagramSocket(RECV_PORT);
    ip = InetAddress.getLocalHost();
    recvKey();
    sendKeyConfirm();
    recvMsg();
    decrypt();
    sign();
    encrypt();
    sendMsg();
    System.out.println("EXITING.\n");
  }
}
