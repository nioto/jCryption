/**
 * 
 */
package net.nioto.jcryption;

import javax.servlet.ServletException;

/**
 * Exception specific for JCryption Project
 * 
 * @author tonio
 *
 */
public class JCryptionException extends ServletException {

  /**
   * 
   */
  private static final long serialVersionUID = 7787065744217205383L;

  /**
   * 
   */
  public JCryptionException() {
    super();
  }

  /**
   * @param message
   */
  public JCryptionException(String message) {
    super(message);
  }

  /**
   * @param rootCause
   */
  public JCryptionException(Throwable rootCause) {
    super(rootCause);
  }

  /**
   * @param message
   * @param rootCause
   */
  public JCryptionException(String message, Throwable rootCause) {
    super(message, rootCause);
  }

}
