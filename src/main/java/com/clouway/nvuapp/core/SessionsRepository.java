package com.clouway.nvuapp.core;

import java.time.LocalDateTime;

import java.util.Optional;

public interface SessionsRepository {

    TutorSession register(String tutorId, LocalDateTime instantTime);

    void cleanExpiredSessions(LocalDateTime date);

    void deleteSession(String sessionId);

    Optional<Tutor> findTutorBySessionId(String sessionId, LocalDateTime date);
}
