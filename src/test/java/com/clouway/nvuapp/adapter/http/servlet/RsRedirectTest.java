package com.clouway.nvuapp.adapter.http.servlet;

import com.clouway.nvuapp.adapter.http.servlet.RsRedirect;
import com.clouway.nvuapp.core.Response;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RsRedirectTest {

  @Test
  public void happyPath() throws Exception {
    Response rsRedirect = new RsRedirect("/someurl");
    assertThat(rsRedirect.status(), is(302));
    assertThat(rsRedirect.headers().get("Location"), is("/someurl"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void redirectUrlIsNull() throws Exception {
    new RsRedirect(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void redirectUrlIsEmpty() throws Exception {
    new RsRedirect("");
  }
}