package com.dev.fr13.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document("speech")
public class Speech {
    private static final Speech DEFAULT_SPEECH = new Speech(LocalDate.MIN, Person.getDefaultPerson());

    @Id
    private String id;
    private LocalDate date;
    private Person person;
    private LocalTime duration;

    public Speech(LocalDate date, Person person, LocalTime duration) {
        this.date = date;
        this.person = person;
        this.duration = duration;
    }

    public Speech(LocalDate date, Person person) {
        this(date, person, LocalTime.MIN);
    }

    public Speech() {
    }

    public static Speech getDefaultSpeech() {
        return DEFAULT_SPEECH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPersonName() {
        return this.person.getName();
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Speech speech = (Speech) o;

        if (id != null ? !id.equals(speech.id) : speech.id != null) return false;
        if (!date.equals(speech.date)) return false;
        return person.equals(speech.person);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + date.hashCode();
        result = 31 * result + person.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Speech{" +
                ", date=" + date +
                ", person=" + person +
                ", duration=" + duration +
                '}';
    }
}