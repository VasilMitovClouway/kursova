package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsFreemarker;
import com.clouway.nvuapp.adapter.http.servlet.RsRedirect;
import com.clouway.nvuapp.adapter.http.servlet.RsWithCookies;
import com.clouway.nvuapp.core.PageHandler;
import com.clouway.nvuapp.core.Request;
import com.clouway.nvuapp.core.Response;
import com.clouway.nvuapp.core.SessionsRepository;
import com.clouway.nvuapp.core.Tutor;
import com.clouway.nvuapp.core.TutorRepository;
import com.clouway.nvuapp.core.TutorSession;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class LoginHandler implements PageHandler {
  public final TutorRepository repository;
  public final SessionsRepository sessionsRepository;
  private static final int ONE_MINUTE = 60 * 1000;

  public LoginHandler(SessionsRepository sessionsRepository, TutorRepository repository) {
    this.repository = repository;
    this.sessionsRepository = sessionsRepository;
  }

  @Override
  public Response handle(Request req) {
    String tutorID = req.param("LoginID");
    String password = req.param("LoginPass");
    List<Tutor> tutors = repository.findTutor(tutorID);
    if (ifBothAreNullOrEmpty(tutorID, password)) {
      return new RsFreemarker("login.html", Collections.singletonMap("message", ""));
    }
    if (ifOneIsNullOrEmpty(tutorID, password)) {
      return new RsFreemarker("login.html", Collections.singletonMap("message", "Моля попълнете всичките полета!"));
    }
    if (!tutors.isEmpty() && tutors.get(0).password.equals(password)) {
      return startSession(forward(tutorID), tutorID, LocalDateTime.now().withNano(0));
    }
    return new RsFreemarker("login.html", Collections.singletonMap("message", "Грешно ID или парола!"));
  }


  private boolean ifOneIsNullOrEmpty(String tutorID, String password) {
    return ((tutorID == null || tutorID.isEmpty()) || (password == null || password.isEmpty()));
  }

  private boolean ifBothAreNullOrEmpty(String tutorID, String password) {
    return ((tutorID == null || tutorID.isEmpty()) && (password == null || password.isEmpty()));
  }

  private Response forward(String tutorId) {
    if ("admin".equals(tutorId)) {
      return new RsRedirect("/adminHome");
    }
    return new RsRedirect("/");
  }

  private Response startSession(Response response, String tutorId, LocalDateTime instantTime) {
    TutorSession session = sessionsRepository.register(tutorId, instantTime);
    Cookie cookie = new Cookie("SID", session.id);
    cookie.setMaxAge(session.lifeSpan * ONE_MINUTE);
    return new RsWithCookies(cookie, response);
  }
}
