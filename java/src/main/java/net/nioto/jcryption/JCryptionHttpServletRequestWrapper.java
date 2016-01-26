package net.nioto.jcryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * An {@link HttpServletRequestWrapper} created based on a query String
 * coming from the decoding of Jcryption parameter 
 * 
 * @author tonio
 */

public class JCryptionHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private static final String UTF8 = StandardCharsets.UTF_8.displayName();
  
  Map<String, List<String>> parameters;
  String queryString;
  

  public JCryptionHttpServletRequestWrapper(HttpServletRequest request, String queryString) 
  throws JCryptionException {
    super(request);
    String encoding = request.getCharacterEncoding() == null ? UTF8 : request.getCharacterEncoding();
    this.queryString = queryString;
    try {
      this.parameters  = Utils.splitQuery( this.queryString, encoding );
    } catch ( UnsupportedEncodingException uee ) {
      throw new JCryptionException( "Unsupported character encoding " + encoding , uee);
    }
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServletRequestWrapper#getQueryString()
   */
  @Override
  public String getQueryString() {
    return this.queryString;
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
   */
  @Override
  public String getParameter(String name) {
    List<String> list = this.parameters.get( name );
    if( list == null || list.size() == 0 ) {
      return null;
    }
    return list.get(0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequestWrapper#getParameterMap()
   */
  @Override
  public Map<String, String[]> getParameterMap() {
    Map<String, String[]> map = new HashMap<String, String[]>();
    for (Map.Entry<String, List<String>> entry : this.parameters.entrySet()) {
      List<String> list = entry.getValue();
      String[] val = null;
      if( list != null && list.size() > 0 ) {
        val = list.toArray( new String[list.size()]);
      }
      map.put( entry.getKey(), val);
    }
    return Collections.unmodifiableMap(map);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequestWrapper#getParameterNames()
   */
  @Override
  public Enumeration<String> getParameterNames() {
    return Collections.enumeration( this.parameters.keySet());
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
   */
  @Override
  public String[] getParameterValues(String name) {
    List<String> list = this.parameters.get( name );
    if( list == null || list.size() == 0 ) {
      return null;
    }
    return list.toArray( new String[list.size()]);
  }

}
