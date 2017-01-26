package com.clouway.nvuapp.adapter.persistence;

import com.clouway.nvuapp.core.QuestionRepository;
import com.clouway.nvuapp.core.Question;
import org.junit.Before;
import org.junit.Test;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentQuestionRepositoryTest {
  private DataStore datastore = new DataStore(new ConnectionProvider());
  private TableManager tableManager = new TableManager(datastore);

  @Before
  public void setUp() throws Exception {
    tableManager.truncateTable("QUESTIONS");
  }

  @Test
  public void happyPath() throws Exception {
    QuestionRepository questionRepository = new PersistentQuestionRepository(datastore);
    Question question = new Question("test", "A1", 1, 2, 3, 4, "q", "a", "b", "c");
    assertThat(questionRepository.register(question),is("Въпросът е регистриран успешно."));
    assertThat(questionRepository.findQuestionMatching("test", "A1", "1", "2", "3", "4","q").get(),is(new Question("test","A1",1,2,3,4,"q","a","b","c")));
  }

  @Test
  public void questionAlreadyExists() throws Exception {
    QuestionRepository questionRepository = new PersistentQuestionRepository(datastore);
    Question question = new Question("test", "B3", 2, 2, 5, 3, "q", "a", "b", "c");
    questionRepository.register(question);
    questionRepository.register(question);
    assertThat(questionRepository.register(question),is("Вече има такъв регистриран въпрос."));
  }

  @Test
  public void getUserQuestions() throws Exception {
    QuestionRepository repository = new PersistentQuestionRepository(datastore);
    Question question1 = new Question("tut1", "B3", 2, 2, 5, 3, "tutor1 question", "answer", "answer", "answer");
    Question question2 = new Question("tut2", "B3", 2, 2, 5, 3, "q", "a", "b", "c");

    repository.register(question1);
    repository.register(question2);

    assertThat(repository.getQuestions("tut1").size(), is(1));
    assertThat(repository.getQuestions("tut1").get(0), is(new Question("tut1", "B3", 2, 2, 5, 3, "tutor1 question", "answer", "answer", "answer")));
  }

  @Test
  public void getAllQuestions() throws Exception {
    QuestionRepository repository = new PersistentQuestionRepository(datastore);
    Question question1 = new Question("tut1", "B3", 2, 2, 5, 3, "tutor1 question", "answer", "answer", "answer");
    Question question2 = new Question("tut2", "B3", 2, 2, 5, 3, "q", "a", "b", "c");

    repository.register(question1);
    repository.register(question2);

    assertThat(repository.getQuestions().size(), is(2));
  }
}