package com.clouway.nvuapp.core;

import com.clouway.nvuapp.adapter.http.controllers.LoginHandler;
import com.clouway.nvuapp.FakeRequest;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;
import com.clouway.nvuapp.adapter.persistence.ConnectionProvider;
import com.clouway.nvuapp.adapter.persistence.PersistentSessionRepository;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.clouway.nvuapp.core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class LoginHandlerTest {
  private JUnitRuleMockery context = new JUnitRuleMockery();
  private SessionsRepository sessions = new PersistentSessionRepository(new DataStore(new ConnectionProvider()), 10);
  private TutorRepository repository = context.mock(TutorRepository.class);
  private LoginHandler loginHandler = new LoginHandler(sessions, repository);
  private List<Tutor> tutors = new LinkedList<>();


  @Test
  public void happyPath() throws Exception {
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<>());
    fakeRequest.addParam("LoginID", "");
    fakeRequest.addParam("LoginPass", "");
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("");
      will(returnValue(tutors));
    }});
    Response response = loginHandler.handle(fakeRequest);
    assertThat(reader().read(response), containsString("</form>\n" +
            "    \n" +
            "</body>"));
  }

  @Test
  public void emptyID() throws Exception {
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<>());
    fakeRequest.addParam("LoginID", "");
    fakeRequest.addParam("LoginPass", "11111");
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("");
      will(returnValue(tutors));
    }});
    Response response = loginHandler.handle(fakeRequest);
    assertThat(reader().read(response), containsString("Моля попълнете всичките полета!"));
    ;
  }

  @Test
  public void emptyPass() throws Exception {
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<>());
    fakeRequest.addParam("LoginID", "11111");
    fakeRequest.addParam("LoginPass", "");
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("11111");
      will(returnValue(tutors));
    }});
    Response response = loginHandler.handle(fakeRequest);
    assertThat(reader().read(response), containsString("Моля попълнете всичките полета!"));
    ;
  }

  @Test
  public void wrongPassAndID() throws Exception {
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<>());
    fakeRequest.addParam("LoginID", "55555");
    fakeRequest.addParam("LoginPass", "55555");
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("55555");
      will(returnValue(tutors));
    }});
    Response response = loginHandler.handle(fakeRequest);
    assertThat(reader().read(response), containsString("Грешно ID или парола!"));
  }

  @Test
  public void wrongPass() throws Exception {
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<>());
    fakeRequest.addParam("LoginID", "11111");
    fakeRequest.addParam("LoginPass", "22222");
    tutors.add(new Tutor("11111", "11111"));
    context.checking(new Expectations() {{
      oneOf(repository).findTutor("11111");
      will(returnValue(tutors));
    }});
    Response response = loginHandler.handle(fakeRequest);
    assertThat(reader().read(response), containsString("Грешно ID или парола!"));
  }

  @Test
  public void correctPassAndId() throws Exception {
    FakeRequest fakeRequest = new FakeRequest(new LinkedHashMap<>());
    fakeRequest.addParam("LoginID", "11111");
    fakeRequest.addParam("LoginPass", "11111");
    tutors.add(new Tutor("11111", "11111"));
    context.checking(new Expectations() {{
      oneOf(repository).register(new Tutor("11111", "11111"));
      oneOf(repository).findTutor("11111");
      will(returnValue(tutors));
    }});
    Response response = loginHandler.handle(fakeRequest);
    assertThat(response.headers().get("Location"), equalTo("/"));
  }
}
