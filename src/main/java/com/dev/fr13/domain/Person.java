package com.dev.fr13.domain;

public class Person {
    private static final Person DEFAULT_PERSON = new Person("undefined");

    private String name;

    public Person(String name) {
        this.name = name;
    }

    public static Person getDefaultPerson() {
        return DEFAULT_PERSON;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}