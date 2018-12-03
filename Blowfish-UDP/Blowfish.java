/*
 * Blowfish.java
 *
 * @author: Steven Grecu (sgg0003@auburn.edu)
 * @created: 11/28/2018
 * @version: 11/29/2018
 */

import java.io.File;
import java.io.FileInputStream;

public class Blowfish {
  int P[] = new int[18];
  int S[][] = new int[4][256];

  
  private Blowfish() {
    // initialize P and S reading in PI
    try {
       FileInputStream fis = new FileInputStream(new File("pi_hex_1m.txt"));
       String nums = "";
       for (int i = 0; i < P.length; i++) {
         for (int j = 0; j < 8; j++) {
           nums += (char) fis.read();
         }
         P[i] = Integer.parseUnsignedInt(nums, 16);
         nums = "";
       }
       for (int i = 0; i < S.length; i++) {
         for (int j = 0; j < S[i].length; j++) {
           for (int k = 0; k < 8; k++) {
             nums += (char) fis.read();
           }
           S[i][j] = Integer.parseUnsignedInt(nums, 16);
           nums = "";
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }

    /* Copied from Wikipedia; modify to get working
    for (int i=0 ; i<18 ; ++i)
      P[i] ^= key[i % keylen];
    */
  }
        
  private int f(int x) {
    return -1;
  }

  /* THIS IS THE METHOD WIKI CALLS "encrypt"
  private static void encryptHelper(uint32_t & L, uint32_t & R) {
    for (int i=0 ; i<16 ; i += 2) {
      L ^= P[i];
      R ^= f(L);
      R ^= P[i+1];
      L ^= f(R);
    }
    L ^= P[16];
    R ^= P[17];
    swap (L, R);
  }
  */
  
  public static String encrypt(String plaintext, byte[] key) {
    return "";

    /* Copied from Wikipedia; modify to get working
    uint32_t L = 0, R = 0;
    for (int i=0 ; i<18 ; i+=2) {
      encryptHelper(L, R);
      P[i] = L; P[i+1] = R;
    }
    for (int i=0 ; i<4 ; ++i)
      for (int j=0 ; j<256; j+=2) {
        encryptHelper(L, R);
        S[i][j] = L; S[i][j+1] = R;
      }
    */
  }

  /* This is the method WIKI calls "decrypt"
  private static void decryptHelper(uint32_t & L, uint32_t & R) {
    for (int i=16 ; i > 0 ; i -= 2) {
      L ^= P[i+1];
      R ^= f(L);
      R ^= P[i];
      L ^= f(R);
    }
    L ^= P[1];
    R ^= P[0];
    swap (L, R);
  }
  */

  public static String decrypt(String ciphertext, byte[] key) {
    return "";

    /* Very similar to encrypt(), just in reverse. */
  }

  
  /*
   * DO TESTING HERE.
   */
  public static void main(String[] args) throws Exception {
    new Blowfish();
    String testText = "This is a test.";
    byte[] testKey = "password".getBytes();
    System.out.print("Original Key (in hex):");
    for (byte b: testKey)
      System.out.print(" " + Integer.toString(((int) b), 16));
    System.out.println("\nOriginal plaintext: " + testText);
    
    String encTestText = encrypt(testText, testKey);
    System.out.println("The encrypted text: " + encTestText);
    
    String decTestText = encrypt(testText, testKey);
    System.out.println("The decrypted text: " + decTestText);
  }
}
