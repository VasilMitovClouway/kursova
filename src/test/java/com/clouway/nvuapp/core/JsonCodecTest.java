package com.clouway.nvuapp.core;

import com.clouway.nvuapp.core.JsonCodec;
import com.clouway.nvuapp.core.Question;
import com.clouway.nvuapp.core.Questionnaire;
import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class JsonCodecTest {
  @Test
  public void happyPath() throws Exception {
    JsonCodec codec = new JsonCodec();
    Question question = new Question("admin", "A1", 1, 2, 3, 4, "q", "a", "b", "c");
    Questionnaire questionnaire = new Questionnaire(1);
    questionnaire.addQuestions(new LinkedList<Question>() {{
      add(question);
    }});
    String data = codec.marshallToString(questionnaire);
    Questionnaire unmarshalledQuestionnaire = codec.unmarshall(data);
    assertThat(questionnaire, is(unmarshalledQuestionnaire));

  }
}