package com.clouway.nvuapp.core;

import javax.servlet.http.Cookie;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface Request {
  String param(String name);

  Cookie cookie(String cookieName);
}
