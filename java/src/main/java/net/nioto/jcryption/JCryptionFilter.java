package net.nioto.jcryption;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import net.nioto.jcryption.keys.AbstractServerKey;
import net.nioto.jcryption.keys.GeneratedServerKey;
import net.nioto.jcryption.keys.OpenSSLServerKey;

public class JCryptionFilter implements Filter {

  private static final String ATTSESSION_KEY = JCryptionFilter.class.getName();
  
  private AbstractServerKey serverKeys;
  
  public JCryptionFilter() {
    // empty constructor
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    
    String publicKeyFilepath = config.getInitParameter("public_key_path");
    String privateKeyFilePath = config.getInitParameter("private_key_path");
    
    this.serverKeys = new GeneratedServerKey();
    
    if( !Utils.isNullOrEmpty( publicKeyFilepath ) && !Utils.isNullOrEmpty( privateKeyFilePath )){
      this.serverKeys = new OpenSSLServerKey(publicKeyFilepath, privateKeyFilePath);
    } else if( Utils.isNullOrEmpty( publicKeyFilepath ) && Utils.isNullOrEmpty( privateKeyFilePath )){
      this.serverKeys = new GeneratedServerKey();
    } else {
      throw new JCryptionException("Only one parameter of { public_key_path, private_key_path } is present" );
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if( "true".equals( request.getParameter("getPublicKey")) ) {
      // return public Key
      sendPublicKey(response);
      return;
    }
    if( "true".equals( request.getParameter("handshake")) ) {
      // return handshake
      HttpServletRequest req = (HttpServletRequest) request;
      HttpSession session = req.getSession(true);
      String clientKey = sendHandshake(request.getParameter("key"), response);
      session.setAttribute( ATTSESSION_KEY, clientKey);
      return;
    }
    String jCryption = request.getParameter("jCryption");
    if ( null != jCryption && !"".equals(jCryption)) {
      HttpServletRequest req = (HttpServletRequest) request;
      HttpSession session = req.getSession(true);
      String clientKey = (String)session.getAttribute(ATTSESSION_KEY);
      byte[] decrypted;
      try {
          decrypted = CryptUtils.decryptAES( clientKey, jCryption.getBytes() );
      } catch ( GeneralSecurityException gse) {
        throw new ServletException( "an error occured", gse);
      }
      String queryString = new String(decrypted);
      JCryptionHttpServletRequestWrapper wrapper = new JCryptionHttpServletRequestWrapper(req, queryString);
      // decode here
      chain.doFilter(wrapper, response);
      return;
    }
    // decode here
    chain.doFilter(request, response);
  }
  
  private String sendHandshake(String param, ServletResponse response) throws IOException,ServletException{
    String received;
    byte[] data;
    try {
      byte[] encryptedData = Utils.decode64(param);
      data = this.serverKeys.decrypt(encryptedData);
      received = new String(data);
    } catch (Exception e) {
      throw new ServletException(e);
    }
    
    try {
      String challenge = CryptUtils.encryptAES( received, data);
      sendJson(response, "challenge", challenge);
      return received;
    } catch (Exception e) {
      throw new ServletException(e);
    }    
  }
  private void sendPublicKey(ServletResponse response) throws IOException,ServletException{
    sendJson(response, "publickey", this.serverKeys.getPublicKeyAsString());
  }
  
  private void sendJson(ServletResponse response, String jsonKey, Object value) 
  throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("Content-type: application/json; charset=utf-8");
    JSONObject obj = new JSONObject();
    obj.put(jsonKey, value);
    obj.write(response.getWriter());
    response.flushBuffer();
  }
  
  @Override
  public void destroy() {
    // do nothing
  }
}
