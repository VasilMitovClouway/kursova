package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsFreemarker;
import com.clouway.nvuapp.core.Request;
import com.clouway.nvuapp.core.Response;
import com.clouway.nvuapp.core.SecuredHandler;
import com.clouway.nvuapp.core.Tutor;

import java.util.Collections;

public class AdminHomePageHandler implements SecuredHandler {
  @Override
  public Response handle(Request req, Tutor tutor) {
    return new RsFreemarker("adminHome.html", Collections.<String, Object>emptyMap());
  }
}