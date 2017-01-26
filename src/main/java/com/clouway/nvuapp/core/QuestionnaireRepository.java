package com.clouway.nvuapp.core;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface QuestionnaireRepository {
  void register(Questionnaire questionnaire);

  void update(Questionnaire questionnaire);

  Questionnaire getLastOrNewQuestionnaire();
}
