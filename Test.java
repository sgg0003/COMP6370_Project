import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Test {
   private static String  line;
   private static Scanner scan;

   public static void main(String[] args) {
      try {
         scan = new Scanner(new File("contract.txt"));
         while (scan.hasNextLine()) {
            line = scan.nextLine();
            System.out.println(line);
         }
         scan.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      
      // Create a Datagram Socket

      // Create a Datagram Packet

      // Invoke send() or receive() on a packet
      
   }
   
}
