import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/*
 * Blowfish.java
 *
 * @author: Steven Grecu (sgg0003@auburn.edu)
 * @created: 11/28/2018
 * @version: 11/29/2018
 * Used the following as a guide for F():
 *     https://github.com/Rupan/blowfish/blob/master/blowfish.c
 */

public class Blowfish {
  int P[] = new int[18];
  int S[][] = new int[4][256];

  
  public Blowfish(byte[] key) {
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

    // XOR P-array with key-bits
    for (int i = 0; i < 18; i++)
      P[i] ^= key[i % key.length];
    
    // Loop to replace P and S
    int L = 0, R = 0;
    int[] LR = new int[2];
    for (int i = 0; i < 18; i += 2) {
      LR = encryptHelper(L, R);
      L = LR[0];
      R = LR[1];
      P[i] = L;
      P[i+1] = R;
    }
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 256; j += 2) {
        LR = encryptHelper(L, R);
        L = LR[0];
        R = LR[1];
        S[i][j] = L;
        S[i][j+1] = R;
      }
    }
  }
  
  private int f(int x) {
    short a, b, c, d;
    int y;
    
    d = (short)(x & 0xFF);
    x >>= 8;
    c = (short)(x & 0xFF);
    x >>= 8;
    b = (short)(x & 0xFF);
    x >>= 8;
    a = (short)(x & 0xFF);
    y = S[0][a] + S[1][b];
    y ^= S[2][c];
    y += S[3][d];
    return y;
  }

  /* THIS IS THE METHOD WIKI CALLS "encrypt" */
  private int[] encryptHelper(int L, int R) {
    for (int i = 0; i < 16; i += 2) {
      L ^= P[i];
      R ^= f(L);
      R ^= P[i+1];
      L ^= f(R);
    }
    L ^= P[16];
    R ^= P[17];
    // Store R first, then L to effectively swap them
    return new int[]{R, L};
  }
  
  public String encrypt(String plaintext, byte[] key) throws Exception {
    // Break plaintext into successive 32-bit halves to encrypt.
    byte[] bytePlainText = plaintext.getBytes("ISO-8859-1");
    byte[] byteCipherText;
    String ciphertext = "";
    byte[] Lbyte = new byte[4];
    byte[] Rbyte = new byte[4];
    int len = bytePlainText.length;

    for (int i = 0; i < len; i += 8) {
      if (i+4 >= len) {
        Lbyte = new byte[4];
        for (int j = 0; j < len-i; j++) {
          Lbyte[j] = bytePlainText[i+j];
        }
        Rbyte = new byte[4];
      } else {
        Lbyte = Arrays.copyOfRange(bytePlainText, i, i+4);
        if (i+8 > len) {
          Rbyte = new byte[4];
          for (int j = 0; j < len-i-4; j++) {
            Lbyte[j] = bytePlainText[i+4+j];
          }
        } else {
          Rbyte = Arrays.copyOfRange(bytePlainText, i+4, i+8);
        }
      }
      ByteBuffer bbl = ByteBuffer.wrap(Lbyte);
      ByteBuffer bbr = ByteBuffer.wrap(Rbyte);
      int L = bbl.getInt();
      int R = bbr.getInt();
      int[] LR = encryptHelper(L, R);
      byteCipherText = ByteBuffer.allocate(4).putInt(LR[0]).array();
      ciphertext += new String(byteCipherText, "ISO-8859-1");
      byteCipherText = ByteBuffer.allocate(4).putInt(LR[1]).array();
      ciphertext += new String(byteCipherText, "ISO-8859-1");
    }
    
    return ciphertext;
  }

  /* This is the method WIKI calls "decrypt" */
  private int[] decryptHelper(int L, int R) {
    for (int i = 16; i > 0; i -= 2) {
      L ^= P[i+1];
      R ^= f(L);
      R ^= P[i];
      L ^= f(R);
    }
    L ^= P[1];
    R ^= P[0];
    return new int[]{R, L};
  }

  /* Very similar to encrypt(), just in reverse. */
  public String decrypt(String ciphertext, byte[] key) throws Exception {
    byte[] byteCipherText = ciphertext.getBytes("ISO-8859-1");
    byte[] bytePlainText;
    String plaintext = "";
    byte[] Lbyte = new byte[4];
    byte[] Rbyte = new byte[4];
    int len = byteCipherText.length;
    
    for (int i = 0; i < len; i += 8) {
      if (i+4 >= len) {
        Lbyte = new byte[4];
        Lbyte = Arrays.copyOfRange(byteCipherText, i, len);
      } else {
        Lbyte = Arrays.copyOfRange(byteCipherText, i, i+4);
        if (i+8 > len) {
          Rbyte = new byte[4];
          Rbyte = Arrays.copyOfRange(byteCipherText, i+4, len);
        } else {
          Rbyte = Arrays.copyOfRange(byteCipherText, i+4, i+8);
        }
      }
      ByteBuffer bbl = ByteBuffer.wrap(Lbyte);
      ByteBuffer bbr = ByteBuffer.wrap(Rbyte);
      int L = bbl.getInt();
      int R = bbr.getInt();
      int[] LR = decryptHelper(L, R);
      bytePlainText = ByteBuffer.allocate(4).putInt(LR[0]).array();
      plaintext += new String(bytePlainText, "ISO-8859-1");
      bytePlainText = ByteBuffer.allocate(4).putInt(LR[1]).array();
      plaintext += new String(bytePlainText, "ISO-8859-1");
    }
    return plaintext;
  }

  
  /*
   * DO TESTING HERE.
   */
  public static void main(String[] args) throws Exception {
    /*
    byte[] testKey = "password".getBytes("ISO-8859-1");
    Blowfish bf = new Blowfish(testKey);
    String plaintext = "";
    byte[] byteString = "this is a test".getBytes("ISO-8859-1");
    byte[] Lbyte = Arrays.copyOfRange(byteString, 0, 4);
    byte[] Rbyte = Arrays.copyOfRange(byteString, 4, 8);
    ByteBuffer bbl = ByteBuffer.wrap(Lbyte);
      ByteBuffer bbr = ByteBuffer.wrap(Rbyte);
      int L = bbl.getInt();
      int R = bbr.getInt();
      int[] LR = bf.encryptHelper(L, R);
      LR = bf.decryptHelper(LR[0], LR[1]);
      byte[] bytePlainText = ByteBuffer.allocate(4).putInt(LR[0]).array();
      plaintext += new String(bytePlainText);
      bytePlainText = ByteBuffer.allocate(4).putInt(LR[1]).array();
      plaintext += new String(bytePlainText);
    */  
    
    String testText = "Contract: This is an agreement between Alice (Buyer) and Bob (Seller) to purchase Property XYZ at Lake Martin, Alabama, for the price of $900,000.  Signed: Alice (Buyer)  Date: November 27, 2018.";
    byte[] testKey = "password".getBytes("ISO-8859-1");
    Blowfish bf = new Blowfish(testKey);
    System.out.print("Original Key (in hex):");
    for (byte b: testKey)
      System.out.print(" " + Integer.toString(((int) b), 16));
    System.out.println("\nOriginal plaintext: " + testText);
    
    String encTestText = bf.encrypt(testText, testKey);
    System.out.println("The encrypted text: " + encTestText);
    
    String decTestText = bf.decrypt(encTestText, testKey);
    System.out.println("The decrypted text: " + decTestText);
  }
}
