package com.clouway.nvuapp.adapter.http.servlet;

import com.google.common.collect.ImmutableMap;
import com.clouway.nvuapp.core.PageHandler;
import com.clouway.nvuapp.core.PageRegistry;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class ServerPageRegistry implements PageRegistry {

  private ImmutableMap<String, PageHandler> handlers;
  private PageHandler defaultHandler;

  public ServerPageRegistry(ImmutableMap<String, PageHandler> handlers, PageHandler defaultHandler) {
    this.handlers = handlers;
    this.defaultHandler = defaultHandler;
  }

  @Override
  public PageHandler findHandler(String requestURI) {
    if (handlers.containsKey(requestURI)) {
      return handlers.get(requestURI);
    }
    return defaultHandler;
  }
}
