package com.clouway.nvuapp.core;

import com.clouway.nvuapp.core.Response;
import com.clouway.nvuapp.core.Tutor;
import org.hamcrest.*;

import javax.servlet.http.Cookie;
import java.util.Optional;

public class CustomMatchers {

    public static Matcher<Response> isRedirectingTo(String expectedUrl) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                String location = response.headers().get("Location");
                return (location == null && expectedUrl == null) || expectedUrl.equals(location);
            }

            @Override
            protected void describeMismatchSafely(Response item, Description mismatchDescription) {
                mismatchDescription.appendText("was ");
                mismatchDescription.appendValue(item.headers().get("Location"));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("redirect to be to ");
                description.appendValue(expectedUrl);
            }
        };
    }

    public static Matcher<Response> isStatusEqualTo(Integer expectedStatus) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                Integer status = response.status();
                return status.equals(expectedStatus);
            }


            @Override
            protected void describeMismatchSafely(Response item, Description mismatchDescription) {
                mismatchDescription.appendText("was ");
                mismatchDescription.appendValue(item.status());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected status is ");
                description.appendValue(expectedStatus);
            }
        };
    }

    public static Matcher<Optional<Tutor>> isPresent() {
        return new TypeSafeMatcher<Optional<Tutor>>() {
            @Override
            protected boolean matchesSafely(Optional<Tutor> tutor) {
               return tutor.isPresent();
            }

            @Override
            protected void describeMismatchSafely(Optional<Tutor> item, Description mismatchDescription) {
                mismatchDescription.appendText("was ");
                mismatchDescription.appendValue(item.isPresent());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected ");
                description.appendValue(true);
            }
        };
    }

    public static Matcher<Optional<Tutor>> isNotPresent() {
        return new TypeSafeMatcher<Optional<Tutor>>() {
            @Override
            protected boolean matchesSafely(Optional<Tutor> tutor) {
                return !tutor.isPresent();
            }

            @Override
            protected void describeMismatchSafely(Optional<Tutor> item, Description mismatchDescription) {
                mismatchDescription.appendText("was ");
                mismatchDescription.appendValue(item.isPresent());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected ");
                description.appendValue(false);
            }
        };
    }

    public static Matcher<Cookie> isDead() {
        return new TypeSafeMatcher<Cookie>() {
            @Override
            protected boolean matchesSafely(Cookie cookie) {
                return cookie.getMaxAge() == 0;
            }

            @Override
            protected void describeMismatchSafely(Cookie item, Description mismatchDescription) {
                mismatchDescription.appendText("was ");
                mismatchDescription.appendValue(item.getMaxAge());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected ");
                description.appendValue(true);
            }
        };
    }
}
