package com.clouway.nvuapp;

import com.clouway.nvuapp.core.Request;

import javax.servlet.http.Cookie;
import java.util.LinkedHashMap;
import java.util.Map;

public class FakeRequest implements Request {
  private Map<String, Object> params = new LinkedHashMap<>();

  public FakeRequest(Map<String, Object> params) {
    this.params = params;
  }

  @Override
  public String param(String name) {
    return String.valueOf(params.get(name));
  }

  @Override
  public Cookie cookie(String cookieName) {
    return null;
  }

  public void addParam(String name, String param) {
    params.put(name, param);
  }

  public void setParams(Map params) {
    this.params = params;
  }
}