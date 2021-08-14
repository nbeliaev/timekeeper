package com.dev.fr13.persistence.service;

import com.dev.fr13.domain.Person;
import com.dev.fr13.domain.Speech;
import com.dev.fr13.persistence.reps.SpeechRepository;
import com.dev.fr13.skype.domain.MostValuableSpeech;
import com.dev.fr13.util.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpeechService {
    private static final Logger log = LoggerFactory.getLogger(SpeechService.class);

    private static final int MAX_AVAILABLE_SPEECH_DURATION = 5;
    private static final LocalTime DEFAULT_SPEECH_DURATION = LocalTime.of(0, MAX_AVAILABLE_SPEECH_DURATION, 0);

    private final SpeechRepository repository;

    public SpeechService(SpeechRepository repository) {
        this.repository = repository;
    }

    public void saveAll(List<Speech> speeches) {
        repository.saveAll(speeches);
    }

    public Optional<Person> findPerson(Person person) {
        // overhead
        return repository.findFirstByPerson(person).map(Speech::getPerson);
    }

    public Optional<Speech> findSpeechWithMaxDuration() {
        return repository.findFirstByOrderByDurationDesc();
    }

    public Optional<Speech> findSpeechWithMaxDuration(Person person) {
        return repository.findAllByPerson(person).stream()
                .max(Comparator.comparing(Speech::getDuration));
    }

    public Optional<Speech> findSpeechWithMinDuration() {
        return doFindSpeechWithMinDuration(repository.findAll());
    }

    public Optional<Speech> findSpeechWithMinDuration(Person person) {
        return doFindSpeechWithMinDuration(repository.findAllByPerson(person));
    }

    public Optional<MostValuableSpeech> findMostAccurateSpeech() {
        var speeches = repository.findAll();
        var total = computeTotalMeetingNumberByPersons(speeches);
        var failed = computeFailedMeetingNumberByPersons(speeches);

        var temp = new ArrayList<MostValuableSpeech>();
        var opt = total.entrySet().stream()
                .filter(i -> !failed.containsKey(i.getKey()))
                .findFirst();
        if (opt.isPresent()) {
            var entry = opt.get();
            temp.add(new MostValuableSpeech(entry.getKey(), 100.0));
        }

        total.forEach((k, v) -> {
            if (failed.containsKey(k)) {
                temp.add(new MostValuableSpeech(k, 100 - (failed.get(k) / (double) v) * 100));
            }
        });

        return temp.stream().max(Comparator.comparingDouble(MostValuableSpeech::getPercents));
    }

    public Optional<MostValuableSpeech> calculatePercentAccurateMeetings(Person person) {
        var speeches = repository.findAllByPerson(person);
        var total = computeTotalMeetingNumberByPersons(speeches);
        var failed = computeFailedMeetingNumberByPersons(speeches);

        if (failed.isEmpty()) {
            return Optional.of(new MostValuableSpeech(person, 100));
        } else {
            var percents = 100 - (failed.get(person) / (double) total.get(person)) * 100;
            return Optional.of(new MostValuableSpeech(person, percents));
        }
    }

    public Optional<MostValuableSpeech> findMostInaccurateSpeech() {
        var speeches = repository.findAll();
        var total = computeTotalMeetingNumberByPersons(speeches);
        var failed = computeFailedMeetingNumberByPersons(speeches);

        var temp = new ArrayList<MostValuableSpeech>();
        total.forEach((k, v) -> {
            if (failed.containsKey(k)) {
                temp.add(new MostValuableSpeech(k, 100 - (failed.get(k) / (double) v) * 100));
            }
        });

        return temp.stream().min(Comparator.comparingDouble(MostValuableSpeech::getPercents));
    }

    public long calculateTotalMeetingsNumber() {
        return doCalculateTotalMeetingNumber(repository.findAll());
    }

    public long calculateTotalMeetingsNumber(Person person) {
        return doCalculateTotalMeetingNumber(repository.findAllByPerson(person));
    }

    public long calculateSkippedMeetingsNumberByPerson(Person person) {
        return repository.findAllByPerson(person).stream()
                .filter(i -> i.getDuration().equals(LocalTime.MIN))
                .count();
    }

    public LocalDateTime calculateTotalSpeechDuration() {
        return doCalculateTotalSpeechDuration(repository.findAll());
    }

    public LocalDateTime calculateTotalSpeechDuration(Person person) {
        return doCalculateTotalSpeechDuration(repository.findAllByPerson(person));
    }

    public LocalTime calculateAvgDurationPerMeeting() {
        var totalMeetingsNumber = calculateTotalMeetingsNumber();
        if (totalMeetingsNumber == 0) {
            return LocalTime.MIN;
        }
        var totalMeetingsDuration = calculateTotalSpeechDuration();
        return doCalculateAvgTimePerMeeting(totalMeetingsNumber, totalMeetingsDuration);
    }

    public LocalTime calculateAvgDurationPerMeeting(Person person) {
        var totalMeetingsNumber = calculateTotalMeetingsNumber(person);
        if (totalMeetingsNumber == 0) {
            return LocalTime.MIN;
        }
        var totalMeetingsDuration = calculateTotalSpeechDuration(person);
        return doCalculateAvgTimePerMeeting(totalMeetingsNumber, totalMeetingsDuration);
    }

    private Map<Person, Long> computeTotalMeetingNumberByPersons(List<Speech> list) {
        return list.stream()
                .filter(i -> i.getDuration().isAfter(LocalTime.MIN))
                .collect(Collectors.groupingBy(Speech::getPerson, Collectors.counting()));
    }

    private Map<Person, Long> computeFailedMeetingNumberByPersons(List<Speech> list) {
        return list.stream()
                .filter(i -> i.getDuration().isAfter(LocalTime.MIN))
                .filter(i -> i.getDuration().isAfter(DEFAULT_SPEECH_DURATION))
                .collect(Collectors.groupingBy(Speech::getPerson, Collectors.counting()));
    }

    private Optional<Speech> doFindSpeechWithMinDuration(List<Speech> list) {
        return list.stream()
                .filter(i -> i.getDuration().isAfter(LocalTime.MIN))
                .min(Comparator.comparing(Speech::getDuration));
    }

    private long doCalculateTotalMeetingNumber(List<Speech> list) {
        return list.stream()
                .filter(i -> i.getDuration().isAfter(LocalTime.MIN))
                .map(Speech::getDate)
                .distinct()
                .count();
    }

    private LocalDateTime doCalculateTotalSpeechDuration(List<Speech> list) {
        return list.stream()
                .map(Speech::getDuration)
                .map(DateTimeConverter::localTimeToLocalDateTime)
                .reduce(
                        LocalDateTime.of(0, 1, 1, 0, 0, 0),
                        (i1, i2) -> i1.plus(i2.toLocalTime().toSecondOfDay(), ChronoUnit.SECONDS));
    }

    private LocalTime doCalculateAvgTimePerMeeting(long totalMeetingsNumber, LocalDateTime totalMeetingsDuration) {
        var sec = DateTimeConverter.getSecondDuration(totalMeetingsDuration) / totalMeetingsNumber;
        return LocalTime.ofSecondOfDay(sec);
    }

    private List<Speech> findAll() {
        return repository.findAll();
    }
}