/*
 * Alice.java
 *
 * @author: Steven Grecu (sgg0003@auburn.edu)
 * @created: 11/28/2018
 * @version: 11/28/2018
 */


public class Alice {
   private static Alice alice;
   private String key;
   private String message;
   private String recvMsg;

   public static Alice getInstance() {
      if (alice == null)
         alice = new Alice();
      return alice;
   }         
   
   public void createKey() {
      ;
   }
   
   public void sendKey(Bob recvr) {
      ;
   }
   
   public void sendMsg(Bob recvr) {
      ;
   }
   
   public void recvMsg() {
      ;
   }

   public void encrypt() {
      Blowfish.encrypt(message, key);
   }
   
   public void decrypt() {
      Blowfish.decrypt(recvMsg, key);
   }
   
   public void verifySig() {
      ;
   }
}
