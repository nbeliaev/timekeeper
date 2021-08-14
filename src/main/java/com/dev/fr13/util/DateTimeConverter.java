package com.dev.fr13.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final String TIME_FORMAT = "H:mm:ss";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    private static final int SECONDS_PER_DAY = 86_400;
    private static final int SECONDS_PER_HOUR = 3_600;
    private static final int SECONDS_PER_MINUTE = 60;

    public static LocalTime stringToTime(String s) {
        return LocalTime.parse(s, TIME_FORMATTER);
    }

    public static LocalDate stringToDate(String s) {
        return LocalDate.parse(s, DATE_FORMATTER);
    }

    public static long getSecondDuration(LocalDateTime t) {
        long d = t.getDayOfYear() - 1L;
        long h = t.getHour();
        long m = t.getMinute();
        long s = t.getSecond();
        return (d * SECONDS_PER_DAY) + (h * SECONDS_PER_HOUR) + (m * SECONDS_PER_MINUTE) + s;
    }

    public static LocalDateTime localTimeToLocalDateTime(LocalTime t) {
        return LocalDateTime.of(0, 1, 1, t.getHour(), t.getMinute(), t.getSecond());
    }

    private DateTimeConverter() {
        throw new UnsupportedOperationException();
    }
}
