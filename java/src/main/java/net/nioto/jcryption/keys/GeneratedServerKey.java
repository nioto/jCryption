/**
 * 
 */
package net.nioto.jcryption.keys;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import net.nioto.jcryption.JCryptionException;
import net.nioto.jcryption.Utils;

/**
 * @author tonio
 *
 */
public class GeneratedServerKey extends AbstractServerKey {

  private static String ALGORITHM = "RSA";
  private static int SIZE = 1024;

  private PrivateKey privateKey;
  private PublicKey publicKey;
  /**
   * 
   */
  public GeneratedServerKey() throws JCryptionException {
    super();
    // Get the public/private key pair
    KeyPairGenerator keyGen;
    try {
      keyGen = KeyPairGenerator.getInstance(ALGORITHM);
    } catch (GeneralSecurityException gse) {
      throw new JCryptionException(" unable to get an instanc of KeyPairGenerator ", gse);
    }
    keyGen.initialize(SIZE);
    KeyPair keyPair = keyGen.genKeyPair();
    this.privateKey = keyPair.getPrivate();
    this.publicKey = keyPair.getPublic();

    byte[] publicKeyBytes = this.publicKey.getEncoded();
    // Convert to OPENSSL format
    StringBuilder sb = new StringBuilder("-----BEGIN PUBLIC KEY-----")
      .append("\n").append(Utils.encode64(publicKeyBytes))
      .append("-----END PUBLIC KEY-----");
    setPublicKey( sb.toString() );
  }

  @Override
  public byte[] decrypt(byte[] encryptedData) throws JCryptionException {
    try {
      Cipher rsa;
      rsa = Cipher.getInstance(ALGORITHM);
      rsa.init(Cipher.DECRYPT_MODE, this.privateKey);
      byte[] bytes = rsa.doFinal(encryptedData);
      return bytes;
    } catch (GeneralSecurityException gse) {
      throw new JCryptionException("Unable to decrypt message", gse);
    }
  }
}
