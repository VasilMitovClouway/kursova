package com.clouway.nvuapp.adapter.http.servlet;

import com.clouway.nvuapp.core.Response;

import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class FakeResponse implements Response {
  private byte[] bytes;
  private Integer status;
  private Map<String, String> headers = new LinkedHashMap<>();
  private Cookie cookie = new Cookie("notNull", "");

  public FakeResponse(String text, Integer status, Map<String, String> headers) {
    this.bytes = text.getBytes();
    this.status = status;
    this.headers = headers;
  }

  public FakeResponse(String body, Integer status) {
    this.bytes = body.getBytes();
    this.status = status;
  }

  public FakeResponse(Integer status, Map<String, String> headers) {
    this.status = status;
    this.headers = headers;
  }

  @Override
  public InputStream body() throws IOException {
    return new ByteArrayInputStream(bytes);
  }

  @Override
  public Map<String, String> headers() {
    return headers;
  }

  @Override
  public int status() {
    return status;
  }

  @Override
  public List<Cookie> cookies() {
    return Collections.emptyList();
  }

  public void assertHasHeader(String header) {
    assertTrue(headers.containsKey(header));
  }
}
