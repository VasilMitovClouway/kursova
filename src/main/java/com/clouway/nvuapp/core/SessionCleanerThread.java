package com.clouway.nvuapp.core;

import com.clouway.nvuapp.core.SessionsRepository;

import java.time.LocalDateTime;

public class SessionCleanerThread extends Thread {
    private static final int ONE_HOUR = 1000 * 60 * 60;
    private SessionsRepository repository;

    public SessionCleanerThread(SessionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(ONE_HOUR);
                repository.cleanExpiredSessions(LocalDateTime.now());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
