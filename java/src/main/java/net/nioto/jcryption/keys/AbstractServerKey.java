package net.nioto.jcryption.keys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import net.nioto.jcryption.JCryptionException;

public abstract class AbstractServerKey {

  private String publickeyStr=null;

  protected AbstractServerKey() {
    /**
     * Add bouncyCastleProvider as Security Provider
     */
    Security.addProvider(new BouncyCastleProvider());
  }

  public final String getPublicKeyAsString(){
    return this.publickeyStr;
  }

  protected final void setPublicKey(String key){
    this.publickeyStr= key;
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
  public abstract byte[] decrypt(byte[] encryptedData)
      throws JCryptionException;
 }