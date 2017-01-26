package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsFreemarker;
import com.clouway.nvuapp.core.QuestionRepository;
import com.clouway.nvuapp.core.Request;
import com.clouway.nvuapp.core.Response;
import com.clouway.nvuapp.core.SecuredHandler;
import com.clouway.nvuapp.core.Tutor;

import java.util.Collections;

public class QuestionListHandler implements SecuredHandler {
  private final QuestionRepository questionRepository;

  public QuestionListHandler(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  @Override
  public Response handle(Request req, Tutor tutor) {
    return new RsFreemarker(
            "questionList.html", Collections.<String, Object>singletonMap("questionList", questionRepository.getQuestions(tutor.tutorId)));
  }
}