/*
 * Main.java
 * @author: Steven Grecu (sgg0003@auburn.edu)
 * @created: 11/28/2018
 * @version: 11/28/2018
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
   private static String readContract() {
      String line = "";
      try {
         Scanner scan = new Scanner(new File("contract.txt"));
         while (scan.hasNextLine())
            line += scan.nextLine();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      return line;
   }
   
   private static void writeContract(String contract) {
      // Copy received message to new file
      Charset charset = Charset.forName("US-ASCII");
      Path path = Paths.get("signed_contract.txt");
      try (BufferedWriter writer =
           Files.newBufferedWriter(path, charset)) {
         writer.write(contract);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) throws IOException {
      Alice alice = Alice.getInstance();
      Bob bob     = Bob.getInstance();
   
      alice.createKey();
      alice.sendKey();
      bob.recvKey();
      bob.sendKeyConfirm();
      alice.setMessage(readContract());
      alice.encrypt();
      alice.sendMsg();
      bob.recvMsg();
      bob.decrypt();
      bob.sign();
      bob.encrypt();
      bob.sendMsg();
      alice.recvMsg();
      alice.decrypt();
      alice.verifySig();
      writeContract(alice.getMessage());
   
      /*****************
       *    TESTING    *
       *****************/
      
   }
}