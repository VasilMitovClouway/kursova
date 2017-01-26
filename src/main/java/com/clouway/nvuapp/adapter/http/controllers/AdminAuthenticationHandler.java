package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsWithCookies;
import com.clouway.nvuapp.core.*;
import com.clouway.nvuapp.adapter.http.servlet.RsRedirect;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Optional;

public class AdminAuthenticationHandler implements PageHandler {
    private final SessionsRepository sessions;
    private final SecuredHandler handler;

    public AdminAuthenticationHandler(SessionsRepository sessions, SecuredHandler handler) {
        this.sessions = sessions;
        this.handler = handler;
    }

    @Override
    public Response handle(Request req) {
        Cookie cookie = req.cookie("SID");
        if (cookie == null) {
            return new RsRedirect("/login");
        }
        Optional<Tutor> possibleTutor = sessions.findTutorBySessionId(cookie.getValue(), LocalDateTime.now().withNano(0));
        if (!possibleTutor.isPresent()) {
            cookie.setMaxAge(-1);
            return new RsRedirect("/login");
        }
        if (!"admin".equals(possibleTutor.get().tutorId)){
            return new RsRedirect("/");
        }
        return new RsWithCookies(cookie, handler.handle(req, possibleTutor.get()));
    }
}
