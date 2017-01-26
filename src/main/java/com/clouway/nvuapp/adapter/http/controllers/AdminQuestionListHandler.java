package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.adapter.http.servlet.RsFreemarker;
import com.clouway.nvuapp.core.Question;
import com.clouway.nvuapp.core.QuestionRepository;
import com.clouway.nvuapp.core.Request;
import com.clouway.nvuapp.core.Response;
import com.clouway.nvuapp.core.SecuredHandler;
import com.clouway.nvuapp.core.Tutor;
import com.clouway.nvuapp.core.TutorRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminQuestionListHandler implements SecuredHandler {
  private final QuestionRepository questionRepository;
  private final TutorRepository tutorRepository;

  public AdminQuestionListHandler(QuestionRepository questionRepository, TutorRepository tutorRepository) {
    this.questionRepository = questionRepository;
    this.tutorRepository = tutorRepository;
  }

  @Override
  public Response handle(Request req, Tutor tutor) {
    List<Question> allQuestions = questionRepository.getQuestions();
    if (req.param("tutorId") == null) {
      return new RsFreemarker("adminQuestionList.html", ImmutableMap.of("questionList", allQuestions,
              "tutors", tutorRepository.allTutors()));
    }
    List<Question> filteredQuestions = allQuestions
            .stream()
            .filter(it -> filterBy(getFilterSet(req), it))
            .collect(Collectors.toList());
    return new RsFreemarker("adminQuestionList.html", ImmutableMap.of("questionList", filteredQuestions,
            "tutors", tutorRepository.allTutors()));
  }

  private boolean filterBy(Set<String> filter, Question it) {
    Set<String> index = it.searchIndex();
    for (String each : filter) {
      if (each.contains("non")) {
        continue;
      }
      if (!index.contains(each)) {
        return false;
      }
    }
    return true;
  }

  private Set<String> getFilterSet(Request req) {
    String tutorId = req.param("tutorId");
    String category = req.param("category");
    String module = req.param("module");
    String subModl = req.param("submodule");
    String theme = req.param("theme");
    if (theme.equals("0")) {
      theme = "non";
    }
    String diff = req.param("difficulty");
    return Sets.newHashSet("tutorId:" + tutorId, "category:" + category, "module:" + module, "subModule:" + subModl, "theme:" + theme, "difficulty:" + diff);
  }
}