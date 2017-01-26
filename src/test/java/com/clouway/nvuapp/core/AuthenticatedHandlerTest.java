package com.clouway.nvuapp.core;

import com.clouway.nvuapp.adapter.http.controllers.AuthenticatedHandler;
import com.clouway.nvuapp.adapter.http.controllers.HomeHandler;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.clouway.nvuapp.core.CustomMatchers.isRedirectingTo;
import static com.clouway.nvuapp.core.CustomMatchers.isStatusEqualTo;
import static org.junit.Assert.assertThat;

public class AuthenticatedHandlerTest {
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void happyPath() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new HomeHandler();
        AuthenticatedHandler authenticatedHandler = new AuthenticatedHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(new Cookie("SID", "someValue")));

            oneOf(sessions).findTutorBySessionId("someValue", LocalDateTime.now().withNano(0));
            will(returnValue(Optional.of(new Tutor("tutor", ""))));
        }});

        Response response  = authenticatedHandler.handle(request);

        assertThat(response, isStatusEqualTo(200));
        assertThat(response, isRedirectingTo(null));
    }

    @Test
    public void noAvailableCookie() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new HomeHandler();
        AuthenticatedHandler authenticatedHandler = new AuthenticatedHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(null));
        }});

        Response response  = authenticatedHandler.handle(request);

        assertThat(response, isStatusEqualTo(302));
        assertThat(response, isRedirectingTo("/login"));
    }

    @Test
    public void noSessionAvailableForThatUser() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new HomeHandler();
        AuthenticatedHandler authenticatedHandler = new AuthenticatedHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(new Cookie("SID", "someValue")));

            oneOf(sessions).findTutorBySessionId("someValue", LocalDateTime.now().withNano(0));
            will(returnValue(Optional.empty()));
        }});

        Response response  = authenticatedHandler.handle(request);

        assertThat(response, isStatusEqualTo(302));
        assertThat(response, isRedirectingTo("/login"));
    }
}
