package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsRedirect;
import com.clouway.nvuapp.adapter.http.servlet.RsWithCookies;
import com.clouway.nvuapp.core.PageHandler;
import com.clouway.nvuapp.core.Request;
import com.clouway.nvuapp.core.Response;
import com.clouway.nvuapp.core.SessionsRepository;

import javax.servlet.http.Cookie;

public class LogoutHandler implements PageHandler {
  private SessionsRepository repository;

  public LogoutHandler(SessionsRepository repository) {
    this.repository = repository;
  }

  @Override
  public Response handle(Request req) {
    Cookie cookie = req.cookie("SID");
    repository.deleteSession(cookie.getValue());
    cookie.setMaxAge(0);
    return new RsWithCookies(cookie, new RsRedirect("/login"));
  }
}
