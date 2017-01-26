package com.clouway.nvuapp.core;

import com.clouway.nvuapp.FakeRequest;
import com.clouway.nvuapp.adapter.http.controllers.AdminQuestionListHandler;
import com.clouway.nvuapp.adapter.http.controllers.InMemoryQuestionRepository;
import com.google.common.collect.Lists;


import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.clouway.nvuapp.core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class AdminQuestionListHandlerTest {
  private TutorRepository inMemoTutorRepo = new TutorRepository() {
    private List<Tutor> tutors = new LinkedList<Tutor>() {{
      add(new Tutor("vasil", ""));
      add(new Tutor("1234", ""));
      add(new Tutor("admin", ""));
    }};

    @Override
    public void register(Tutor tutor) {

    }

    @Override
    public List<Tutor> findTutor(String id) {
      return null;
    }

    @Override
    public List<Tutor> allTutors() {
      return tutors;
    }
  };
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void adminLoadAllQuestions() throws Exception {
    Request request = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("tutorId", "non");
      put("category", "non");
      put("module", "non");
      put("submodule", "non");
      put("theme", "non");
      put("difficulty", "non");
    }});

    QuestionRepository repository = new InMemoryQuestionRepository(
            Lists.newArrayList(
                    new Question("vasil", "CAT1", 23, 1, 1, 1, "User-1234 How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("admin", "CAT2", 23, 1, 1, 1, "User-1234 How you feel today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("1234", "CAT1", 23, 1, 1, 1, "User-0987 How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("admin", "CAT2", 23, 1, 1, 1, "User-0987 How you feel today?", "I feel Good", "I feel bad", "I feed unusual")
            )
    );
    AdminQuestionListHandler questionListHandler = new AdminQuestionListHandler(repository, inMemoTutorRepo);


    Response response = questionListHandler.handle(request, new Tutor("admin", ""));

    assertThat(reader().read(response), containsString("User-1234 How are you today?"));
    assertThat(reader().read(response), containsString("User-1234 How you feel today?"));

    assertThat(reader().read(response), containsString("User-0987 How are you today?"));
    assertThat(reader().read(response), containsString("User-0987 How you feel today?"));
  }

  @Test
  public void noQuestionsInRepository() throws Exception {
    Request request = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("tutorId", "non");
      put("category", "non");
      put("module", "non");
      put("submodule", "non");
      put("theme", "non");
      put("difficulty", "non");
    }});
    QuestionRepository repository = new InMemoryQuestionRepository(Collections.emptyList());
    AdminQuestionListHandler questionListHandler = new AdminQuestionListHandler(repository, inMemoTutorRepo);

    Response response = questionListHandler.handle(request, new Tutor("admin", ""));

    assertThat(reader().read(response), containsString("Няма регистрирани въпроси до момента"));
  }

  @Test
  public void filteringQuestionsByTutors() throws Exception {
    Request request = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("tutorId", "admin");
      put("category", "non");
      put("module", "non");
      put("submodule", "non");
      put("theme", "non");
      put("difficulty", "non");
    }});

    QuestionRepository repository = new InMemoryQuestionRepository(
            Lists.newArrayList(
                    new Question("admin", "CAT1", 23, 1, 1, 1, "User-1234 How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("admin", "CAT2", 23, 1, 1, 1, "User-1234 How you feel today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("vasil", "CAT1", 23, 1, 1, 1, "User-0987 How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("vasil", "CAT2", 23, 1, 1, 1, "User-0987 How you feel today?", "I feel Good", "I feel bad", "I feed unusual")
            )
    );
    AdminQuestionListHandler questionListHandler = new AdminQuestionListHandler(repository, inMemoTutorRepo);
    Response response = questionListHandler.handle(request, new Tutor("admin", ""));

    assertThat(reader().read(response), containsString("User-1234 How are you today?"));
    assertThat(reader().read(response), containsString("User-1234 How you feel today?"));
  }

  @Test
  public void filteringByDifferentFieldsWithNon() throws Exception {
    Request request = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("tutorId", "vasil");
      put("category", "non");
      put("module", "23");
      put("submodule", "1");
      put("theme", "1");
      put("difficulty", "non");
    }});

    QuestionRepository repository = new InMemoryQuestionRepository(
            Lists.newArrayList(
                    new Question("admin", "CAT1", 23, 1, 1, 1, "User-1234 How are you today my fiend?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("admin", "CAT2", 23, 1, 1, 1, "User-1234 How you feel today user?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("vasil", "CAT1", 23, 1, 1, 1, "User-0987 How are you today my enemy?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("vasil", "CAT2", 23, 1, 1, 3, "User-0987 How you feel today my nemesis?", "I feel Good", "I feel bad", "I feed unusual")
            )
    );
    AdminQuestionListHandler questionListHandler = new AdminQuestionListHandler(repository, inMemoTutorRepo);
    Response response = questionListHandler.handle(request, new Tutor("admin", ""));
    assertThat(reader().read(response), containsString("User-0987 How are you today my enemy?"));
    assertThat(reader().read(response), containsString("User-0987 How you feel today my nemesis?"));
  }
}