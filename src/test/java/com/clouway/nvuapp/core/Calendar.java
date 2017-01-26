package com.clouway.nvuapp.core;

import java.time.LocalDateTime;

public class Calendar {

    public LocalDateTime newDateTime(int day, int month, int year, int hour, int minutes) {
        return LocalDateTime.of(year, month, day, hour, minutes).withNano(0);
    }

}
