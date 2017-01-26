package com.clouway.nvuapp.adapter.http.servlet;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class ResourceServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String resourceName = req.getRequestURI().split("\\/")[2];
    if (resourceName.endsWith(".css")) {
      resp.setContentType("text/css");
    } else if (resourceName.endsWith(".js")) {
      resp.setContentType("text/javascript");
    } else if (resourceName.endsWith(".html")) {
      resp.setContentType("UTF-8");
      resp.setContentType("text/html");
    }

    byte[] content = ByteStreams.toByteArray(ResourceServlet.class.getResourceAsStream(resourceName));
    ServletOutputStream outputStream = resp.getOutputStream();
    outputStream.write(content);
    outputStream.flush();
  }
}

