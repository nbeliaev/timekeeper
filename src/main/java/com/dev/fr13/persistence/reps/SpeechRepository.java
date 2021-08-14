package com.dev.fr13.persistence.reps;

import com.dev.fr13.domain.Person;
import com.dev.fr13.domain.Speech;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpeechRepository extends MongoRepository<Speech, String> {

    Optional<Speech> findFirstByOrderByDurationDesc();

    List<Speech> findAllByPerson(Person person);

    Optional<Speech> findFirstByPerson(Person person);
}
