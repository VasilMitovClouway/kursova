package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsFreemarker;
import com.clouway.nvuapp.core.*;
import com.google.common.base.Strings;


import java.util.Collections;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class TutorHandler implements PageHandler {
  public final TutorRepository repository;

  public TutorHandler(TutorRepository repository) {
    this.repository = repository;
  }

  @Override
  public Response handle(Request req) {
    String tutorID = req.param("TUTOR_ID");
    String password = req.param("PASSWORD");

    if (Strings.isNullOrEmpty(tutorID) || Strings.isNullOrEmpty(password)) {
      return new RsFreemarker("register.html", Collections.singletonMap("message", ""));
    }
    if (!Strings.isNullOrEmpty(tutorID) || !Strings.isNullOrEmpty(password) && !isValid(tutorID, password)) {
      if (repository.findTutor(tutorID).isEmpty()) {
        repository.register(new Tutor(tutorID, password));
        return new RsFreemarker("register.html", Collections.singletonMap("message", "Регистрацията беше успешна!"));
      }
        return new RsFreemarker("register.html", Collections.singletonMap("message", "Вече съществува такъв акаунт."));
    }
    return new RsFreemarker("register.html", Collections.singletonMap("message", ""));
  }

  private boolean isValid(String tutorID, String password) {
    return (tutorID.length() == 5 && password.length() > 5 || password.length() < 10);
  }
}
