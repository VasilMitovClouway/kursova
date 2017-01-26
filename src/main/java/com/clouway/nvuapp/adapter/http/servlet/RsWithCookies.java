package com.clouway.nvuapp.adapter.http.servlet;

import com.clouway.nvuapp.core.Response;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RsWithCookies implements Response {
    private List<Cookie> cookieList = new LinkedList<>();
    private Response response;

    public RsWithCookies(Cookie cookie, Response response) {
        cookieList.add(cookie);
        this.response = response;
    }

    @Override
    public InputStream body() throws IOException {
        return response.body();
    }

    @Override
    public Map<String, String> headers() {
        return response.headers();
    }

    @Override
    public int status() {
        return response.status();
    }

    @Override
    public Iterable<Cookie> cookies() {
        return cookieList;
    }
}
