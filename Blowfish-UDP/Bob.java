/*
 * Bob.java
 *
 * @author: Steven Grecu (sgg0003@auburn.edu)
 * @created: 11/28/2018
 * @version: 11/28/2018
 */


public class Bob {
   private static Bob bob;
   private String key;
   private String message;
   private String recvMsg;

   public static Bob getInstance() {
      if (bob == null)
         bob = new Bob();
      return bob;
   }         
   
   public void recvKey() {
      ;
   }
   
   public void sendKeyConfirm(Alice recv) {
      ;
   }
   
   public void recvMsg() {
      ;
   }
   
   public void sendMsg(Alice recvr) {
      ;
   }
   
   public void encrypt() {
      Blowfish.encrypt(message, key);
   }
   
   public void decrypt() {
      Blowfish.decrypt(recvMsg, key);
   }
   
   public void sign() {
      ;
   }
}
