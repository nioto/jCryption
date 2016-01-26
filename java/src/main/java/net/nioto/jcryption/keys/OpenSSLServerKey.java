/**
 * 
 */
package net.nioto.jcryption.keys;

import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

import net.nioto.jcryption.IOUtils;
import net.nioto.jcryption.JCryptionException;

/**
 * @author tonio
 *
 */
public class OpenSSLServerKey extends AbstractServerKey {

  private String privateKeyContent;
  /**
   * 
   */
  public OpenSSLServerKey(String publicKeyFilepath, String privateKeyFilePath)
  throws JCryptionException {
    super();
    // read keys content and store locally
    try {
      setPublicKey( IOUtils.read( publicKeyFilepath ) );
    } catch (IOException ioe) {
      throw new JCryptionException( "Unable to read a public key", ioe);
    }
    try {
      this.privateKeyContent = IOUtils.read( privateKeyFilePath );
    } catch (IOException ioe) {
      throw new JCryptionException( "Unable to read a private key", ioe);
    }
  }

  @Override
  public byte[] decrypt(byte[] encryptedData)
      throws JCryptionException {
    try {
      return localdecrypt(encryptedData);
    } catch ( Exception e ) {
      throw new JCryptionException( "Unable to decrypt message", e);
    }
  }
  /**
   * Decrypt data using private key
   * 
   * @param encryptedData
   * @return
   * @throws GeneralSecurityException
   * @throws IOException
   * @throws InvalidCipherTextException
   */
  public byte[] localdecrypt(byte[] encryptedData)
      throws GeneralSecurityException, IOException, InvalidCipherTextException {
    StringReader reader = new StringReader( this.privateKeyContent );
    PEMParser pp = new PEMParser(reader);
    PEMKeyPair privatePemObject = (PEMKeyPair) pp.readObject();
    pp.close();
    reader.close();
    final AsymmetricKeyParameter privateKey = PrivateKeyFactory.createKey(privatePemObject.getPrivateKeyInfo());
    AsymmetricBlockCipher e = new RSAEngine();
    e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
    e.init(false, privateKey);
    byte[] hexEncodedCipher = e.processBlock(encryptedData, 0, encryptedData.length);
    return hexEncodedCipher;
  }
}