package com.clouway.nvuapp.adapter.persistence;

import com.clouway.nvuapp.core.SessionsRepository;
import com.clouway.nvuapp.core.Tutor;
import com.clouway.nvuapp.core.TutorSession;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersistentSessionRepository implements SessionsRepository {
    private final DataStore dataStore;
    private final int durationInMinutes;

    public PersistentSessionRepository(DataStore dataStore, int durationInMinutes) {
        this.dataStore = dataStore;
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public TutorSession register(String tutorId, LocalDateTime instantTime) {
        String sessionId = UUID.randomUUID().toString();
        TutorSession tutorSession = new TutorSession(sessionId, durationInMinutes);

        String query = "insert into SESSIONS VALUES(?, ?, ?)";
        dataStore.update(query, tutorSession.id, tutorId, toTimestamp(instantTime.plusMinutes(durationInMinutes)));
        return tutorSession;
    }

    @Override
    public void cleanExpiredSessions(LocalDateTime date) {
        String query = "delete from SESSIONS where EXPIRATION_TIME < ?";
        dataStore.update(query, toTimestamp(date));
    }

    @Override
    public void deleteSession(String sessionId) {
        String query = "delete from SESSIONS where SID = ?";
        dataStore.update(query, sessionId);
    }

    @Override
    public Optional<Tutor> findTutorBySessionId(String sessionId, LocalDateTime date) {
        String query = "select TUTOR_ID from SESSIONS where SID='" + sessionId + "' and EXPIRATION_TIME > '" + toTimestamp(date) + "'";
        List<Tutor> tutors = dataStore.fetchRows(query, resultSet -> new Tutor(resultSet.getString(1), ""));
        if (tutors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tutors.get(0));
    }

    private Timestamp toTimestamp(LocalDateTime date) {
        return Timestamp.valueOf(date);
    }
}