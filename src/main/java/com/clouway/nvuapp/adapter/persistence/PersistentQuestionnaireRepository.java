package com.clouway.nvuapp.adapter.persistence;

import com.clouway.nvuapp.core.QuestionnaireRepository;
import com.clouway.nvuapp.core.JsonCodec;
import com.clouway.nvuapp.core.Questionnaire;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentQuestionnaireRepository implements QuestionnaireRepository {
  private final DataStore dataStore;
  private final JsonCodec codec = new JsonCodec();

  public PersistentQuestionnaireRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public void register(Questionnaire questionnaire) {
    String data = codec.marshallToString(questionnaire);
    String query = "INSERT INTO QUESTIONNAIRES (QUESTIONNAIRE) VALUES(?)";
    dataStore.update(query, data);
  }


  @Override
  public void update(Questionnaire questionnaire) {
    String data = codec.marshallToString(questionnaire);
    String query = "UPDATE QUESTIONNAIRES" +
            " SET QUESTIONNAIRE='" + data + "'" +
            " WHERE ID=" + questionnaire.getID();
    dataStore.update(query);
  }

  @Override
  public Questionnaire getLastOrNewQuestionnaire() {
    String query = "SELECT * FROM QUESTIONNAIRES ORDER BY ID DESC LIMIT 1";
    List<String> result = dataStore.fetchRows(query, resultSet -> resultSet.getString(2));
    if (result.isEmpty()) {
      Questionnaire questionnaire = new Questionnaire(1);
      register(questionnaire);
      return questionnaire;
    }
    Questionnaire questionnaire = codec.unmarshall(result.get(0));
    if (questionnaire.noAnswers()) {
      return questionnaire;
    }
    Questionnaire newQuestionnaire = new Questionnaire(questionnaire.getID() + 1);
    register(newQuestionnaire);
    return newQuestionnaire;
  }
}
