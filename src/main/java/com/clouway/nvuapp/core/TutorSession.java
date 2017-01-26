package com.clouway.nvuapp.core;

public class TutorSession {
    public final String id;
    public final int lifeSpan;

    public TutorSession (String id, int lifeSpanInMinutes) {
        this.id = id;
        this.lifeSpan = lifeSpanInMinutes;
    }
}