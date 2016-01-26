/**
 * 
 */
package net.nioto.jcryption;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author tonio
 *
 */
public class DumpServlet extends GenericServlet {

  /**
   * 
   */
  private static final long serialVersionUID = -7599617757915723946L;

  /**
   * 
   */
  public DumpServlet() {
    // TODO Auto-generated constructor stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.GenericServlet#service(javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse)
   */
  @Override
  public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

    PrintWriter out = res.getWriter();
    res.setContentType("text/plain");
    
    out.write( "==============================\n");
    @SuppressWarnings("unchecked")
    Enumeration<String> parameterNames = req.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String paramName = parameterNames.nextElement();
      out.write(paramName);
      out.write(": \n");
      String[] paramValues = req.getParameterValues(paramName);
      for (int i = 0; i < paramValues.length; i++) {
        String paramValue = paramValues[i];
        out.write("\t" + paramValue);
        out.write("\n");
      }
    }
    out.write( "==============================\n");
    out.write( "Using parameter maps\n");
    @SuppressWarnings("unchecked")
    Map<String, String[]> map = req.getParameterMap();
    for (Map.Entry<String, String[]> element: map.entrySet()) {
      out.write(element.getKey());
      out.write(": \n");
      String[] paramValues = element.getValue();
      for (int i = 0; i < paramValues.length; i++) {
        String paramValue = paramValues[i];
        out.write("\t" + paramValue);
        out.write("\n");
      }
      
    }
    out.close();

  }

}
