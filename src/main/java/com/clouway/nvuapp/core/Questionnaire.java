package com.clouway.nvuapp.core;

import com.google.common.base.Objects;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class Questionnaire {
  private final Integer ID;
  private List<Question> questions = new LinkedList<>();
  private Map<Integer, String> answers = new LinkedHashMap<>();


  public Questionnaire(Integer ID) {
    this.ID = ID;
  }

  public void add(Question question) {
    questions.add(question);
  }

  public void addQuestions(List<Question> resultQuestions) {
    questions.addAll(resultQuestions);
  }

  public Integer getID() {
    return ID;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public Map<Integer, String> getAnswers() {
    return answers;
  }


  public void finish() {
    extractCorrectAnswers();
    shuffleAnswers();
  }

  /**
   * Extracting the correct answers from the questionnaire.Note that this will be done only once
   * when the questionnare is finished registering, meaning that before that the answers will be null thus
   * showing that the questionnaire is not finished yet.
   *
   * @return correct answers for the questionnaire.
   */
  private void extractCorrectAnswers() {
    Integer it = 1;
    for (Question question : questions) {
      answers.put(it, question.getAnswerA());
      it++;
    }
  }

  private void shuffleAnswers() {
    for (Question question : questions) {
      List<String> answers = new LinkedList<String>() {{
        add(question.getAnswerA());
        add(question.getAnswerB());
        add(question.getAnswerC());
      }};
      Collections.shuffle(answers);
      question.setAnswerA(answers.get(0));
      question.setAnswerB(answers.get(1));
      question.setAnswerC(answers.get(2));
    }
    setQuestions(questions);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Questionnaire that = (Questionnaire) o;
    return Objects.equal(ID, that.ID) &&
            Objects.equal(questions, that.questions) &&
            Objects.equal(answers, that.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(ID, questions, answers);
  }

  public boolean isEmpty() {
    if(answers.isEmpty()&&questions.isEmpty()) {
      return true;
    }
    return false;
  }

  public boolean noAnswers() {
    return answers.isEmpty();
  }
}
