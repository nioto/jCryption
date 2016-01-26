package net.nioto.jcryption;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.ssl.OpenSSL;

public class CryptUtils {

  private CryptUtils() {
    // do nothing
  }
  private static String AES_CIPHER="aes-256-cbc";
  
  public static String encryptAES(String key, byte[] data)
  throws GeneralSecurityException, IOException {
    byte[] enc = OpenSSL.encrypt(AES_CIPHER, key.toCharArray(), data, true, true);
    String str = new String(enc);
    str = str.replace("\n", "").replace("\r", "");
    return str;
  }
  
  public static byte[] decryptAES(String key, byte[] data)
  throws IOException, GeneralSecurityException{
    return OpenSSL.decrypt(AES_CIPHER, key.toCharArray(), data );
  }
 
}
