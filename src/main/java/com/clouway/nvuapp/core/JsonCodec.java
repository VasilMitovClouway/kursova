package com.clouway.nvuapp.core;

import com.google.gson.Gson;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class JsonCodec {
  private final Gson gson = new Gson();

  public String marshallToString(Questionnaire questionnaire) {
    return gson.toJson(questionnaire);
  }

  public Questionnaire unmarshall(String questionnaireAsString) {
    return gson.fromJson(questionnaireAsString, Questionnaire.class);
  }
}
