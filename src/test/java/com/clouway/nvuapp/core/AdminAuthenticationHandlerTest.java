package com.clouway.nvuapp.core;

import com.google.common.collect.ImmutableMap;
import com.clouway.nvuapp.adapter.http.controllers.AdminAuthenticationHandler;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.clouway.nvuapp.core.CustomMatchers.isRedirectingTo;
import static com.clouway.nvuapp.core.CustomMatchers.isStatusEqualTo;
import static org.junit.Assert.assertThat;

public class AdminAuthenticationHandlerTest {

    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void happyPath() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new FakeSecuredHandler(200, Collections.emptyMap());
        AdminAuthenticationHandler adminAuthenticationHandler = new AdminAuthenticationHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(new Cookie("SID", "someValue")));

            oneOf(sessions).findTutorBySessionId("someValue", LocalDateTime.now().withNano(0));
            will(returnValue(Optional.of(new Tutor("admin", ""))));
        }});

        Response response  = adminAuthenticationHandler.handle(request);

        assertThat(response, isStatusEqualTo(200));
        assertThat(response, isRedirectingTo(null));
    }

    @Test
    public void noCookieAvailable() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new FakeSecuredHandler(302, ImmutableMap.<String, String>of("Location", "/login"));
        AdminAuthenticationHandler adminAuthenticationHandler = new AdminAuthenticationHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(null));
        }});

        Response response  = adminAuthenticationHandler.handle(request);

        assertThat(response, isStatusEqualTo(302));
        assertThat(response, isRedirectingTo("/login"));
    }

    @Test
    public void noSessionAvailable() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new FakeSecuredHandler(302, ImmutableMap.<String, String>of("Location", "/login"));
        AdminAuthenticationHandler adminAuthenticationHandler = new AdminAuthenticationHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(new Cookie("SID", "someValue")));

            oneOf(sessions).findTutorBySessionId("someValue", LocalDateTime.now().withNano(0));
            will(returnValue(Optional.empty()));
        }});

        Response response  = adminAuthenticationHandler.handle(request);

        assertThat(response, isStatusEqualTo(302));
        assertThat(response, isRedirectingTo("/login"));
    }

    @Test
    public void tutorIsNotAdmin() throws Exception {
        final Request request = context.mock(Request.class);
        final SessionsRepository sessions = context.mock(SessionsRepository.class);
        SecuredHandler securedHandler = new FakeSecuredHandler(302, ImmutableMap.<String, String>of("Location", "/"));
        AdminAuthenticationHandler adminAuthenticationHandler = new AdminAuthenticationHandler(sessions, securedHandler);

        context.checking(new Expectations(){{
            oneOf(request).cookie("SID");
            will(returnValue(new Cookie("SID", "someValue")));

            oneOf(sessions).findTutorBySessionId("someValue", LocalDateTime.now().withNano(0));
            will(returnValue(Optional.of(new Tutor("notAdmin", ""))));
        }});

        Response response  = adminAuthenticationHandler.handle(request);

        assertThat(response, isStatusEqualTo(302));
        assertThat(response, isRedirectingTo("/"));
    }
}
