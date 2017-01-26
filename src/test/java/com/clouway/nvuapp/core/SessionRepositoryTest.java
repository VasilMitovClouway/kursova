package com.clouway.nvuapp.core;

import com.clouway.nvuapp.adapter.persistence.TableManager;
import org.junit.Before;
import org.junit.Test;
import com.clouway.nvuapp.adapter.persistence.ConnectionProvider;
import com.clouway.nvuapp.adapter.persistence.PersistentSessionRepository;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.util.Optional;

import static com.clouway.nvuapp.core.CustomMatchers.isNotPresent;
import static com.clouway.nvuapp.core.CustomMatchers.isPresent;
import static org.junit.Assert.assertThat;

public class SessionRepositoryTest {
    private DataStore dataStore = new DataStore(new ConnectionProvider());
    private TableManager tableManager = new TableManager(dataStore);
    private Calendar calendar = new Calendar();

    @Before
    public void setUp() throws Exception {
        tableManager.truncateTable("SESSIONS");
    }

    @Test
    public void findExpiredSession() throws Exception {
        SessionsRepository sessionsRepository = new PersistentSessionRepository(dataStore, 10);

        TutorSession session1 = sessionsRepository.register("user1", calendar.newDateTime(20, 1, 2017, 2, 30));
        TutorSession session2 = sessionsRepository.register("user2", calendar.newDateTime(25, 1, 2017, 2, 30));

        Optional<Tutor> tutor1 = sessionsRepository.findTutorBySessionId(session1.id, calendar.newDateTime(20, 1, 2017, 2, 41));
        Optional<Tutor> tutor2 = sessionsRepository.findTutorBySessionId(session2.id, calendar.newDateTime(25, 1, 2017, 2, 35));

        assertThat(tutor1, isNotPresent());
        assertThat(tutor2, isPresent());
    }

    @Test
    public void findNotExpiredSession() throws Exception {
        SessionsRepository sessionsRepository = new PersistentSessionRepository(dataStore, 10);

        TutorSession sessionId1 = sessionsRepository.register("tut1", calendar.newDateTime(15, 5, 2016, 2, 30));
        TutorSession sessionId2 = sessionsRepository.register("tut2", calendar.newDateTime(15, 5, 2016, 2, 30));

        Optional<Tutor> tutor1 = sessionsRepository.findTutorBySessionId(sessionId1.id, calendar.newDateTime(15, 5, 2016, 2, 35));
        Optional<Tutor> tutor2 = sessionsRepository.findTutorBySessionId(sessionId2.id, calendar.newDateTime(15, 5, 2016, 2, 35));

        assertThat(tutor1, isPresent());
        assertThat(tutor2, isPresent());

    }

    @Test
    public void findUnknownSession() throws Exception {
        SessionsRepository sessionsRepository = new PersistentSessionRepository(dataStore, 10);

        sessionsRepository.register("tut1", calendar.newDateTime(15, 5, 2016, 2, 30));
        sessionsRepository.register("tut2", calendar.newDateTime(15, 5, 2016, 2, 30));

        Optional<Tutor> tutor1 = sessionsRepository.findTutorBySessionId("someId1", calendar.newDateTime(15, 5, 2016, 2, 35));
        Optional<Tutor> tutor2 = sessionsRepository.findTutorBySessionId("someId2", calendar.newDateTime(15, 5, 2016, 2, 35));

        assertThat(tutor1, isNotPresent());
        assertThat(tutor2, isNotPresent());
    }
}