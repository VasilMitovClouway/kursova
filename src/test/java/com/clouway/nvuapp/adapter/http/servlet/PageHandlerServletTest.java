package com.clouway.nvuapp.adapter.http.servlet;

import com.clouway.nvuapp.core.PageHandler;
import com.clouway.nvuapp.core.PageRegistry;
import com.clouway.nvuapp.core.Request;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.Collections;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PageHandlerServletTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void happyPath() throws Exception {
    final PageRegistry registry = context.mock(PageRegistry.class);
    final PageHandler pageHandler = context.mock(PageHandler.class);
    final HttpServletRequest req = context.mock(HttpServletRequest.class);
    final HttpServletResponse resp = context.mock(HttpServletResponse.class);
    final FakeServletOutputStream servletResponse = new FakeServletOutputStream();
    PageHandlerServlet pageHandlerServlet = new PageHandlerServlet(registry);

    context.checking(new Expectations() {{
      oneOf(req).getRequestURI();
      will(returnValue("/"));

      oneOf(registry).findHandler("/");
      will(returnValue(pageHandler));

      oneOf(pageHandler).handle(with(any(Request.class)));
      will(returnValue(new FakeResponse("Test text for template", HttpURLConnection.HTTP_OK)));

      oneOf(resp).setStatus(200);

      oneOf(resp).getOutputStream();
      will(returnValue(servletResponse));
    }});
    pageHandlerServlet.doGet(req, resp);
    servletResponse.assertHasValue("Test text for template");
  }

  @Test
  public void servletRedirectsWithStatus302() throws Exception {
    final PageRegistry registry = context.mock(PageRegistry.class);
    final PageHandler pageHandler = context.mock(PageHandler.class);
    final HttpServletRequest req = context.mock(HttpServletRequest.class);
    final HttpServletResponse resp = context.mock(HttpServletResponse.class);
    PageHandlerServlet pageHandlerServlet = new PageHandlerServlet(registry);
    FakeResponse fakeResponse = new FakeResponse(HttpURLConnection.HTTP_MOVED_TEMP,
            Collections.singletonMap("Location", "/someOtherPage"));

    context.checking(new Expectations() {{
      oneOf(req).getRequestURI();
      will(returnValue("/"));

      oneOf(registry).findHandler("/");
      will(returnValue(pageHandler));

      oneOf(pageHandler).handle(with(any(Request.class)));
      will(returnValue(fakeResponse));

      oneOf(resp).sendRedirect("/someOtherPage");
    }});
    pageHandlerServlet.doGet(req, resp);
  }
}
