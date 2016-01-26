package net.nioto.jcryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

  private IOUtils() {
    // private empty constructor, to avoid creating unnecessary objects
  }

  /**
   * Try to read a File using a filepath, if not found in FileSystem,, try
   * to read it using {@link ClassLoader#getResourceAsStream(String)}
   * 
   * @param path
   * @return content of the file
   * @throws IOException 
   */

  public static String read(String path) throws IOException {
    File f = new File(path);
    InputStream in;
    if (f.exists()) {
      in = new FileInputStream(f);
    } else {
      in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
    if (in == null) {
      throw new FileNotFoundException("File not found on path : " + path);
    }
    StringBuilder sb = new StringBuilder();
    int c;
    try {
      while ((c = in.read()) != -1) {
        sb.append((char) c);
      }
    } finally {
      closeQuietly(in);
    }
    return sb.toString().trim();
  }

  /**
   * Close a {@link InputStream}, discarding any IOException that can appear
   * 
   * @param io
   */

  public static void closeQuietly(InputStream io) {
    try {
      if (io != null)
        io.close();
    } catch (IOException e) {
    }
  }
  
  /**
   * Test is a file exists knowing its filepath
   */
  
  public static boolean existes(String path) {
    if( path == null) {
      return false;
    }
    File p = new File(path);
    return p.exists() && p.isFile() && p.canRead();
  }
}
