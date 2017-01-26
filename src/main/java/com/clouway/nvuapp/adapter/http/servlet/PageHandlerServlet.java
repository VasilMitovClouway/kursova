package com.clouway.nvuapp.adapter.http.servlet;

import com.google.common.io.ByteStreams;
import com.clouway.nvuapp.core.PageHandler;
import com.clouway.nvuapp.core.PageRegistry;
import com.clouway.nvuapp.core.Response;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PageHandlerServlet extends HttpServlet {
  private PageRegistry pageRegistry;

  public PageHandlerServlet(PageRegistry pageRegistry) {
    this.pageRegistry = pageRegistry;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PageHandler pageHandler = pageRegistry.findHandler(req.getRequestURI());
    Response response = pageHandler.handle(new RequestWrapper(req));

    if (response.status() == HttpURLConnection.HTTP_MOVED_TEMP) {
      copyCookies(response, resp);
      resp.sendRedirect(response.headers().get("Location"));
      return;
    }
    resp.setStatus(response.status());
    OutputStream outputStream = resp.getOutputStream();
    ByteStreams.copy(response.body(), outputStream);
    outputStream.flush();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }

  private void copyCookies(Response response, HttpServletResponse httpResponse) {
      for (Cookie each : response.cookies()) {
        httpResponse.addCookie(each);
      }
  }
}
