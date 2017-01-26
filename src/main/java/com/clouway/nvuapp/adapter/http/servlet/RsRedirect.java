package com.clouway.nvuapp.adapter.http.servlet;

import com.google.common.collect.ImmutableMap;
import com.clouway.nvuapp.core.Response;

import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RsRedirect implements Response {
  private String redirectUrl;

  public RsRedirect(String redirectUrl) {
    checkArgument(!isNullOrEmpty(redirectUrl), "redirectUrl cannot be null or empty");
    this.redirectUrl = redirectUrl;
  }

  @Override
  public InputStream body() {
    return new ByteArrayInputStream(new byte[]{});
  }

  @Override
  public Map<String, String> headers() {
    return ImmutableMap.of("Location", redirectUrl);
  }

  @Override
  public int status() {
    return HttpURLConnection.HTTP_MOVED_TEMP;
  }

  @Override
  public Iterable<Cookie> cookies() {
    return Collections.emptyList();
  }
}
