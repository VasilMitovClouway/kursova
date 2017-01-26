package com.clouway.nvuapp.core;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class Question {
  private final String tutorId;
  private final String category;
  private final Integer module;
  private final Integer subModule;
  private final Integer theme;
  private final Integer difficulty;
  private final String question;
  private String answerA;
  private String answerB;
  private String answerC;

  public Question(String tutorId, String category, Integer mod, Integer subMod, Integer theme, Integer diff, String question, String answerA, String answerB, String answerC) {
    this.tutorId = tutorId;
    this.category = category;
    this.module = mod;
    this.subModule = subMod;
    this.theme = theme;
    this.difficulty = diff;
    this.question = question;
    this.answerA = answerA;
    this.answerB = answerB;
    this.answerC = answerC;
  }

  public String getTutorId() {
    return tutorId;
  }

  public String getCategory() {
    return category;
  }

  public Integer getModule() {
    return module;
  }

  public Integer getSubModule() {
    return subModule;
  }

  public Integer getTheme() {
    return theme;
  }

  public Integer getDifficulty() {
    return difficulty;
  }

  public String getQuestion() {
    return question;
  }

  public String getAnswerA() {
    return answerA;
  }

  public String getAnswerB() {
    return answerB;
  }

  public String getAnswerC() {
    return answerC;
  }

  public void setAnswerA(String answerA) {
    this.answerA = answerA;
  }

  public void setAnswerB(String answerB) {
    this.answerB = answerB;
  }

  public void setAnswerC(String answerC) {
    this.answerC = answerC;
  }

  public Set<String> searchIndex(){
    return Sets.newHashSet("tutorId:"+tutorId,"category:"+category,"module:"+module,"subModule:"+subModule,"theme:"+theme,"difficulty:"+difficulty);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Question question1 = (Question) o;
    return Objects.equal(tutorId, question1.tutorId) &&
            Objects.equal(category, question1.category) &&
            Objects.equal(module, question1.module) &&
            Objects.equal(subModule, question1.subModule) &&
            Objects.equal(theme, question1.theme) &&
            Objects.equal(difficulty, question1.difficulty) &&
            Objects.equal(question, question1.question) &&
            Objects.equal(answerA, question1.answerA) &&
            Objects.equal(answerB, question1.answerB) &&
            Objects.equal(answerC, question1.answerC);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tutorId, category, module, subModule, theme, difficulty, question, answerA, answerB, answerC);
  }
}
