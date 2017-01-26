package com.clouway.nvuapp.core;

import com.clouway.nvuapp.FakeRequest;
import com.clouway.nvuapp.adapter.http.controllers.InMemoryQuestionRepository;
import com.clouway.nvuapp.adapter.http.controllers.QuestionListHandler;
import com.google.common.collect.Lists;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import java.util.Collections;


import static com.clouway.nvuapp.core.QuestionBuilder.aNewQuestion;
import static com.clouway.nvuapp.core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class QuestionListHandlerTest {
  private final JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void listFewQuestion() throws Exception {
    final Request request = new FakeRequest(Collections.emptyMap());

    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(
            Lists.newArrayList(
                    aNewQuestion().tutorId("1234").question("This is a question 1").answerA("True answer 1").build(),
                    aNewQuestion().tutorId("1234").question("This is a question 2").answerA("True answer 2").build(),
                    aNewQuestion().tutorId("1234").question("This is a question 3").answerA("True answer 3").build())
    );

    final QuestionListHandler questionListHandler = new QuestionListHandler(questionRepository);

    Response response = questionListHandler.handle(request, new Tutor("1234", ""));

    assertThat(reader().read(response), containsString("This is a question 1"));
    assertThat(reader().read(response), containsString("True answer 1"));

    assertThat(reader().read(response), containsString("This is a question 2"));
    assertThat(reader().read(response), containsString("True answer 2"));

    assertThat(reader().read(response), containsString("This is a question 3"));
    assertThat(reader().read(response), containsString("True answer 3"));
  }

  @Test
  public void noQuestionInRepository() throws Exception {
    final Request request = context.mock(Request.class);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(Collections.emptyList());
    final QuestionListHandler questionListHandler = new QuestionListHandler(questionRepository);

    Response response = questionListHandler.handle(request, new Tutor("::any tutor id::", ""));

    assertThat(reader().read(response), containsString("Няма добавени въпроси до момента"));
  }

  @Test
  public void tutorHasNoQuestions() throws Exception {
    final Request request = context.mock(Request.class);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(
            Lists.newArrayList(aNewQuestion().question("This is a question 1").answerA("True answer 1").build()
            ));

    final QuestionListHandler questionListHandler = new QuestionListHandler(questionRepository);

    Response response = questionListHandler.handle(request, new Tutor("1234", ""));

    assertThat(reader().read(response), containsString("Няма добавени въпроси до момента"));
  }
}