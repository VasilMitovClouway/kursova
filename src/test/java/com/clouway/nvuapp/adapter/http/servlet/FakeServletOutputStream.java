package com.clouway.nvuapp.adapter.http.servlet;

import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class FakeServletOutputStream extends ServletOutputStream {

  private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Override
  public void write(int b) throws IOException {
    outputStream.write(b);
  }

  public void assertHasValue(String text) {
    assertThat(outputStream.toString(), is(equalTo(text)));
  }
}
