package com.clouway.nvuapp.core;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseReader {

  public String read(Response response) throws IOException {
    return new String(ByteStreams.toByteArray(response.body()), StandardCharsets.UTF_8);
  }

  public static ResponseReader reader() {
    return new ResponseReader();
  }
}