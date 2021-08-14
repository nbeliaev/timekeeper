package com.dev.fr13.skype.domain;

import com.dev.fr13.domain.Person;

import java.time.LocalDate;
import java.time.LocalTime;

public class MostValuableSpeech {
    private final Person person;
    private final LocalDate date;
    private final LocalTime duration;
    private final double percents;

    public MostValuableSpeech(Person person, double percents) {
        this.person = person;
        this.percents = percents;
        date = LocalDate.MIN;
        duration = LocalTime.MIN;
    }

    public Person getPerson() {
        return person;
    }

    public String getPersonName() {
        return person.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public double getPercents() {
        return percents;
    }
}