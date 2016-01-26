package net.nioto.jcryption;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.ssl.Base64;

public class Utils {

  public static byte[] decode64(String str) {
    return Base64.decodeBase64( str );
  }

  public static String encode64(byte[] bytes) {
    return Base64.encodeBase64String( bytes );
  }
  
  public static boolean isNullOrEmpty(String s) {
    return s== null || s.length()==0;
  }

  /**
   * Parse a query String.
   * 
   * @param queryString the query String to parse
   * @param encoding  the encoding to use for parsing
   * @return a map containing key : paramName, value: values as List<String>
   * @throws UnsupportedEncodingException
   */
  public static Map<String, List<String>> splitQuery(final String queryString, final String encoding)
  throws UnsupportedEncodingException {
    final Map<String, List<String>> query_pairs = new HashMap<String, List<String>>();
    final String[] pairs = queryString.split("&");
    for (String pair : pairs) {
      final int idx = pair.indexOf("=");
      final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), encoding) : pair;
      if (!query_pairs.containsKey(key)) {
        query_pairs.put(key, new LinkedList<String>());
      }
      final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), encoding) : null;
      query_pairs.get(key).add(value);
    }
    return query_pairs;
  }
}
