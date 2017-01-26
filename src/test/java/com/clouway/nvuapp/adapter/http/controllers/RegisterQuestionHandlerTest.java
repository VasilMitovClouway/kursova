package com.clouway.nvuapp.adapter.http.controllers;

import com.clouway.nvuapp.core.*;
import com.clouway.nvuapp.FakeRequest;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.clouway.nvuapp.core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RegisterQuestionHandlerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void happyPath() throws Exception {
    QuestionRepository repository = new InMemoryQuestionRepository(new LinkedList<Question>() {{
      add(new Question("4321", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
    }});
    SecuredHandler registerQuestion = new RegisterQuestionHandler(repository);
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("category", "A1");
      put("module", "1");
      put("submodule", "1");
      put("theme", "1");
      put("difficulty", "1");
      put("question", "");
      put("answerA", "");
      put("answerB", "");
      put("answerC", "");
    }});
    Response response = registerQuestion.handle(fakeRequest, new Tutor("1234", ""));

    assertThat(reader().read(response), containsString("Всички полета трябва да бъдат попълнени."));
  }

  @Test
  public void userFillsFieldsAndRegistersQuestion() throws Exception {
    QuestionRepository repository = new InMemoryQuestionRepository(new LinkedList<Question>() {{
      add(new Question("4321", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
      add(new Question("4321", "A1", 1, 1, 2, 6, "q", "a", "b", "c"));
      add(new Question("4321", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
    }});
    SecuredHandler registerQuestion = new RegisterQuestionHandler(repository);
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("category", "A1");
      put("module", "1");
      put("submodule", "2");
      put("theme", "3");
      put("difficulty", "4");
      put("question", "q");
      put("answerA", "a");
      put("answerB", "b");
      put("answerC", "c");
    }});

    Response response = registerQuestion.handle(fakeRequest, new Tutor("1234", ""));

    assertThat(reader().read(response), containsString("Въпросът е регистриран успешно."));
    assertThat(repository.getQuestions("1234").get(0), is(new Question("1234", "A1", 1, 2, 3, 4, "q", "a", "b", "c")));
  }

  @Test
  public void userTriesToRegisterAnAlreadyExistingQuestion() throws Exception {
    QuestionRepository repository = new InMemoryQuestionRepository(new LinkedList<Question>() {
      {
        add(new Question("1234", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
      }
    });
    SecuredHandler registerQuestion = new RegisterQuestionHandler(repository);
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("category", "A1");
      put("module", "1");
      put("submodule", "2");
      put("theme", "3");
      put("difficulty", "4");
      put("question", "q");
      put("answerA", "a");
      put("answerB", "b");
      put("answerC", "c");
    }});

    Response response = registerQuestion.handle(fakeRequest, new Tutor("1234", ""));

    assertThat(reader().read(response), containsString("Вече има такъв регистриран въпрос."));
  }
}