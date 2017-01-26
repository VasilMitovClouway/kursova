package com.clouway.nvuapp.adapter.persistence;

import com.clouway.nvuapp.core.QuestionnaireRepository;
import com.clouway.nvuapp.core.Question;
import com.clouway.nvuapp.core.Questionnaire;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentQuestionnaireRepositoryTest {
  DataStore dataStore = new DataStore(new ConnectionProvider());
  QuestionnaireRepository repository = new PersistentQuestionnaireRepository(dataStore);
  TableManager tableManager = new TableManager(dataStore);

  @Before
  public void setUp() throws Exception {
    tableManager.truncateTable("QUESTIONNAIRES");
  }

  @After
  public void tearDown() throws Exception {
    tableManager.truncateTable("QUESTIONNAIRES");
  }

  @Test
  public void happyPath() throws Exception {
    Questionnaire questionnaire = new Questionnaire(1);
    repository.register(questionnaire);
    assertThat(questionnaire, is(repository.getLastOrNewQuestionnaire()));
  }

  @Test
  public void updateingQuestionnaire() throws Exception {
    Questionnaire questionnaire = new Questionnaire(1);
    repository.register(questionnaire);
    questionnaire.addQuestions(new LinkedList<Question>() {{
      add(new Question("admin", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
    }});
    repository.update(questionnaire);
    assertThat(questionnaire,is(repository.getLastOrNewQuestionnaire()));
  }
}