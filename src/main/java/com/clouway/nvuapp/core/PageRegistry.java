package com.clouway.nvuapp.core;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface PageRegistry {
  PageHandler findHandler(String requestURI);
}
