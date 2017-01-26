package com.clouway.nvuapp.core;

import com.clouway.nvuapp.adapter.http.controllers.TutorHandler;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.clouway.nvuapp.core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class TutorHandlerTest {
  private JUnitRuleMockery context = new JUnitRuleMockery();
  private TutorRepository repository = context.mock(TutorRepository.class);
  private TutorHandler tutorHandler = new TutorHandler(repository);
  private Request request = context.mock(Request.class);
  private List<Tutor> tutors = new LinkedList<>();

  @Test
  public void happyPath() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).param("TUTOR_ID");
      will(returnValue(null));
      oneOf(request).param("PASSWORD");
      will(returnValue(null));
    }});
    Response response = tutorHandler.handle(request);

    assertThat(reader().read(response), containsString("<p id=\"message\"></p>\n" +
            "    \n" +
            "</body>"));
  }

  @Test
  public void accountDoesntExist() throws Exception {
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("hello");
      will(returnValue(tutors));
      oneOf(repository).register(new Tutor("hello", "hello"));
      oneOf(request).param("TUTOR_ID");
      will(returnValue("hello"));
      oneOf(request).param("PASSWORD");
      will(returnValue("hello"));

    }});
    Response response = tutorHandler.handle(request);
    assertThat(reader().read(response), containsString("Регистрацията беше успешна!"));
  }

  @Test
  public void accountAlreadyExists() throws Exception {
    tutors.add(new Tutor("hello", "hello"));
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("hello");
      will(returnValue(tutors));
      oneOf(request).param("TUTOR_ID");
      will(returnValue("hello"));
      oneOf(request).param("PASSWORD");
      will(returnValue("hello"));
    }});
    Response response = tutorHandler.handle(request);
    assertThat(reader().read(response), containsString("Вече съществува такъв акаунт."));
  }
}
