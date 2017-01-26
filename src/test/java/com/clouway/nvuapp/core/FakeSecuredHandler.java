package com.clouway.nvuapp.core;

import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class FakeSecuredHandler implements SecuredHandler {
    private int status;
    private Map<String, String> headers;

    public FakeSecuredHandler(int status, Map<String, String> headers) {
        this.status = status;
        this.headers = headers;
    }

    @Override
    public Response handle(Request request, Tutor tutor) {
        return new Response(){

            @Override
            public InputStream body() throws IOException {
                return new ByteArrayInputStream(new byte[]{});
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
        };
    }
}
