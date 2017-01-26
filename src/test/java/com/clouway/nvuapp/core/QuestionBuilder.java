package com.clouway.nvuapp.core;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class QuestionBuilder {


  public static QuestionBuilder aNewQuestion() {
    return new QuestionBuilder();
  }

  private String tutorId = "IF000";
  private String category = "A0";
  private Integer mod = 0;
  private Integer subMod = 0;
  private Integer theme = 0;
  private Integer diff = 0;
  private String question = "";
  private String answerA = "";
  private String answerB = "";
  private String answerC = "";

  public QuestionBuilder() {
  }

  public QuestionBuilder tutorId(String tutorId) {
    this.tutorId = tutorId;
    return this;
  }

  public QuestionBuilder category(String category) {
    this.category = category;
    return this;
  }

  public QuestionBuilder mod(Integer mod) {
    this.mod = mod;
    return this;
  }

  public QuestionBuilder subMod(Integer subMod) {
    this.subMod = subMod;
    return this;
  }

  public QuestionBuilder theme(Integer theme) {
    this.theme = theme;
    return this;
  }

  public QuestionBuilder diff(Integer diff) {
    this.diff = diff;
    return this;
  }

  public QuestionBuilder question(String question) {
    this.question = question;
    return this;
  }

  public QuestionBuilder answerA(String answerA) {
    this.answerA = answerA;
    return this;
  }


  public QuestionBuilder answerB(String answerB) {
    this.answerB = answerB;
    return this;
  }


  public QuestionBuilder answerC(String answerC) {
    this.answerC = answerC;
    return this;
  }

  public Question build() {
    return new Question(tutorId, category, mod, subMod, theme, diff, question, answerA, answerB, answerC);
  }
}
