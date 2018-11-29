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
  private static Bob bob;
  private DatagramSocket socket;
  private DatagramPacket packet;
  private InetAddress ip;
  private String message;
  private byte[] key;
  private byte[] buf;

  private final int RECV_PORT   = 8000;
  private final int SEND_PORT   = 9000;
  private final int BUFFER_SIZE = 65535;
  private final String SIGNATURE =
    "Signed: Bob (Seller)  Date: November 29, 2018.";


  // Constructor sets up socket and sets IP address
  // to the local machine.
  private Bob() {
    this.key = new byte[8];
    try {
      this.socket = new DatagramSocket(RECV_PORT);
      this.ip = InetAddress.getLocalHost();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // There should only be one Bob at a time.
  public static Bob getInstance() {
    if (bob == null)
      bob = new Bob();
    return bob;
  }         

  public void recvKey() throws IOException {
    buf = new byte[BUFFER_SIZE];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);
    for (int i = 0; i < key.length; i++)
      key[i] = packet.getData()[i];
  }

  public void sendKeyConfirm() throws IOException {
    message = "I (Bob) have received the key.";
    sendMsg();
  }

  public void recvMsg() throws IOException {
    buf = new byte[BUFFER_SIZE];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);
    message = new String(packet.getData(), 0, packet.getLength());
  }

  public void sendMsg() throws IOException {
    buf = message.getBytes();
    packet = new DatagramPacket(buf, buf.length, ip, SEND_PORT);
    socket.send(packet);
  }

  public void encrypt() {
    message = Blowfish.encrypt(message, key);
  }

  public void decrypt() {
    message = Blowfish.decrypt(message, key);
  }

  public void sign() {
    message = message + "  " + SIGNATURE;
  }
}
