import com.clouway.nvuapp.adapter.http.controllers.*;
import com.clouway.nvuapp.adapter.http.servlet.PageHandlerServlet;
import com.clouway.nvuapp.adapter.http.servlet.ResourceServlet;
import com.clouway.nvuapp.adapter.http.servlet.ServerPageRegistry;
import com.clouway.nvuapp.adapter.persistence.*;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;
import com.clouway.nvuapp.core.PageHandler;
import com.clouway.nvuapp.core.PageRegistry;
import com.clouway.nvuapp.core.SessionCleanerThread;
import com.clouway.nvuapp.core.SessionsRepository;
import com.clouway.nvuapp.core.TutorRepository;
import com.google.common.collect.ImmutableMap;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class JettyMain {
  public static void main(String[] args) {
    final DataStore dataStore = new DataStore(new ConnectionProvider());
    final TutorRepository tutorRepository=new PersistentTutorRepository(dataStore);
    final PersistentQuestionnaireRepository questionnaireRepository = new PersistentQuestionnaireRepository(dataStore);
    final PersistentQuestionRepository questionRepository = new PersistentQuestionRepository(dataStore);
    final SessionsRepository sessions = new PersistentSessionRepository(dataStore, 60);
    final SessionCleanerThread sessionCleaner = new SessionCleanerThread(sessions);
    final PageRegistry registry = new ServerPageRegistry(
            ImmutableMap.<String, PageHandler>builder()
                    .put("/", new AuthenticatedHandler(sessions, new HomeHandler()))
                    .put("/adminHome", new AdminAuthenticationHandler(sessions, new AdminHomePageHandler()))
                    .put("/questions", new AuthenticatedHandler(sessions, new QuestionListHandler(questionRepository)))
                    .put("/adminHome/questions", new AdminAuthenticationHandler(sessions, new QuestionListHandler(questionRepository)))
                    .put("/registerquestion", new AuthenticatedHandler(sessions, new RegisterQuestionHandler(questionRepository)))
                    .put("/adminHome/registerquestion", new AdminAuthenticationHandler(sessions, new RegisterQuestionHandler(questionRepository)))
                    .put("/adminHome/adminQuestions", new AdminAuthenticationHandler(sessions, new AdminQuestionListHandler(questionRepository,tutorRepository)))
                    .put("/registration", new TutorHandler(tutorRepository))
                    .put("/adminHome/questionnaire", new AdminAuthenticationHandler(sessions, new RegisterQuestionnaireHandler(questionRepository, questionnaireRepository)))
                    .put("/adminHome/new-questionnaire", new AdminAuthenticationHandler(sessions, new ShowRegisterQuestionnaireHandler(questionnaireRepository)))
                    .put("/login", new LoginHandler(sessions, new PersistentTutorRepository(dataStore)))
                    .put("/adminHome/finishquestionnaire", new AdminAuthenticationHandler(sessions, new FinishRegisterQuestionnaireHandler(questionnaireRepository)))
                    .put("/logout", new LogoutHandler(sessions))
                    .build(),
            new AuthenticatedHandler(sessions, new HomeHandler())
    );

    Server server = new Server(8080);
    ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContext.setContextPath("/");
    servletContext.addEventListener(new ServletContextListener() {
      @Override
      public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.addServlet("ResourceServlet", new ResourceServlet()).addMapping("/assets/*");
        servletContext.addServlet("pageHandlerServlet", new PageHandlerServlet(registry)).addMapping("/");
      }

      @Override
      public void contextDestroyed(ServletContextEvent servletContextEvent) {
      }
    });
    server.setHandler(servletContext);
    try {
      sessionCleaner.start();
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
