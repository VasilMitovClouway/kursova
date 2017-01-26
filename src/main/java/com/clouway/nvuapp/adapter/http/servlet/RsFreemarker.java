package com.clouway.nvuapp.adapter.http.servlet;

import com.google.common.collect.ImmutableMap;
import com.clouway.nvuapp.core.Response;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RsFreemarker implements Response {
  private final String templateName;
  private final Map<String, Object> values;

  public RsFreemarker(String templateName, Map<String, Object> values) {
    this.templateName = templateName;
    this.values = values;
  }

  @Override
  public InputStream body() throws IOException {
    Configuration configuration = new Configuration();
    configuration.setTemplateLoader(new ClassTemplateLoader(ResourceServlet.class, ""));
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);//To be changed to RETHROW_HANDLER at end

    Template template = configuration.getTemplate(templateName);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try {
      template.process(values, new PrintWriter(bout));
    } catch (TemplateException e) {
      throw new IOException("Got error while trying to evaluate response", e);
    }

    return new ByteArrayInputStream(bout.toByteArray());
  }
//  /home/clouway/clouway/workspaces/idea/bank-t2/src/main/java/com/clouway/nvuapp/adapter/http/servlet/ResourceServlet.java

  @Override
  public Map<String, String> headers() {
    return ImmutableMap.of("Content-Type", "text/html; charset=utf-8");
  }

  @Override
  public int status() {
    return HttpURLConnection.HTTP_OK;
  }

  @Override
  public Iterable<Cookie> cookies() {
    return Collections.emptyList();
  }
}
