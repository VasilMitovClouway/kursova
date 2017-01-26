package com.clouway.nvuapp.core;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface Response {

  InputStream body() throws IOException;

  Map<String, String> headers();

  int status();

  Iterable<Cookie> cookies();
}
