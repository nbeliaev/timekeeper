package com.dev.fr13.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeConverterTest {

    @Test
    void stringToTime() {
        var s = "0:03:51";
        var r = DateTimeConverter.stringToTime(s);
        assertThat(r).isEqualTo(LocalTime.of(0, 3, 51));
    }

    @Test
    void stringToDate() {
        var s = "01.02.2021";
        var r = DateTimeConverter.stringToDate(s);
        assertThat(r).isEqualTo(LocalDate.of(2021, 2, 1));
    }
}