/*
 * Main.java
 * @author: Steven Grecu (sgg0003@auburn.edu)
 * @created: 11/28/2018
 * @version: 11/28/2018
 */


public class Main {

   private static void readContract() {
      ;
   }
   
   private static void writeContract() {
      ;
   }

   public static void main(String[] args) {
      Alice alice = Alice.getInstance();
      Bob bob     = Bob.getInstance();

      alice.createKey();
      alice.sendKey(bob);
      bob.recvKey();
      bob.sendKeyConfirm(alice);
      readContract();
      alice.encrypt();
      alice.sendMsg(bob);
      bob.recvMsg();
      bob.decrypt();
      bob.sign();
      bob.encrypt();
      bob.sendMsg(alice);
      alice.recvMsg();
      alice.decrypt();
      alice.verifySig();
      writeContract();
   }
   
}
