package com.dev.fr13.persistence.service;

import com.dev.fr13.domain.Person;
import com.dev.fr13.domain.Speech;
import com.dev.fr13.persistence.reps.SpeechRepository;
import com.dev.fr13.skype.domain.MostValuableSpeech;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Проверяет сервис по работе с сущностью speech")
class SpeechServiceTest {
    private static final Person PERSON0 = new Person("person0");
    private static final Person PERSON1 = new Person("person1");
    private static final Person PERSON2 = new Person("person2");

    private static final Speech MIN_SPEECH = new Speech(
            LocalDate.of(2021, 1, 8),
            PERSON0,
            LocalTime.of(0, 2, 50));

    private final List<Speech> list = new ArrayList<>();
    @Mock
    private SpeechRepository repository;
    private SpeechService speechService;

    @BeforeEach
    void initList() {

        list.clear();
        list.add(new Speech(
                LocalDate.of(2021, 1, 1),
                PERSON0,
                LocalTime.of(0, 3, 50)));
        list.add(new Speech(
                LocalDate.of(2021, 1, 1),
                PERSON2,
                LocalTime.of(0, 4, 0)));

        list.add(MIN_SPEECH);
        list.add(new Speech(
                LocalDate.of(2021, 1, 8),
                PERSON1,
                LocalTime.of(0, 7, 50)));

        list.add(new Speech(
                LocalDate.of(2021, 1, 15),
                PERSON0,
                LocalTime.of(0, 5, 50)));
        list.add(new Speech(
                LocalDate.of(2021, 1, 15),
                PERSON1));

        MockitoAnnotations.initMocks(this);
        speechService = new SpeechService(repository);
        when(repository.findAll()).thenReturn(list);
        when(repository.findAllByPerson(PERSON0))
                .thenReturn(list.stream().filter(i -> i.getPerson().equals(PERSON0)).collect(Collectors.toList()));
        when(repository.findAllByPerson(PERSON1))
                .thenReturn(list.stream().filter(i -> i.getPerson().equals(PERSON1)).collect(Collectors.toList()));
        when(repository.findAllByPerson(PERSON2))
                .thenReturn(list.stream().filter(i -> i.getPerson().equals(PERSON2)).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("должен найти самый короткий спич")
    void shouldFindSpeechWithMinDuration() {
        var optSpeech = speechService.findSpeechWithMinDuration();
        assertThat(optSpeech).contains(MIN_SPEECH);
    }

    @Test
    @DisplayName("должен найти самый короткий спич сотрудника")
    void shouldFindSpeechWithMinDurationByPerson() {
        var optSpeech = speechService.findSpeechWithMinDuration(PERSON0);
        assertThat(optSpeech).contains(MIN_SPEECH);
    }

    @Test
    @DisplayName("должен найти самого пунктуального сотрудника")
    void shouldFindMostAccuratePerson() {
        var opt = speechService.findMostAccurateSpeech();
        assertThat(opt).isPresent();
        assertThat(opt.get().getPerson()).isEqualTo(PERSON2);
    }

    @Test
    @DisplayName("должен найти процент тайминговых митингов сотрудника")
    void shouldFindPercentPersonAccurateMeetings() {
        var percents = speechService.calculatePercentAccurateMeetings(PERSON0)
                .map(MostValuableSpeech::getPercents).orElse(0.0);
        assertThat(percents).isCloseTo(66.66, Offset.offset(0.1));
    }

    @Test
    @DisplayName("должен найти самого не пунктуального сотрудника")
    void shouldFindMostInaccurateSpeech() {
        var opt = speechService.findMostInaccurateSpeech();
        assertThat(opt).isPresent();
        assertThat(opt.get().getPerson()).isEqualTo(PERSON1);
    }

    @Test
    @DisplayName("должен вернуть общее количество встреч")
    void shouldFindTotalMeetingsNumber() {
        var total = speechService.calculateTotalMeetingsNumber();
        assertThat(total).isEqualTo(3);
    }

    @Test
    @DisplayName("должен вернуть общее количество встреч сотрудника")
    void shouldFindTotalMeetingsNumberByPerson() {
        var total = speechService.calculateTotalMeetingsNumber(PERSON1);
        assertThat(total).isEqualTo(1);
    }

    @Test
    @DisplayName("должен вернуть количество пропущенных митингов")
    void shouldReturnSkippedMeetingsNumberByPerson() {
        var total = speechService.calculateSkippedMeetingsNumberByPerson(PERSON1);
        assertThat(total).isEqualTo(1);
    }

    @Test
    @DisplayName("должен вернуть общее количество времени, потраченного на встречах")
    void shouldFindTotalDuration() {
        var total = speechService.calculateTotalSpeechDuration();
        assertThat(total).isEqualTo(LocalDateTime.of(0, 1, 1, 0, 24, 20));
    }

    @Test
    @DisplayName("должен вернуть общее количество времени спичей сотрудника")
    void shouldFindTotalDurationByPerson() {
        var total = speechService.calculateTotalSpeechDuration(PERSON2);
        assertThat(total).isEqualTo(LocalDateTime.of(0, 1, 1, 0, 4, 0));
    }

    @Test
    @DisplayName("должен вернуть среднее время, затрачиваемое на встречу")
    void shouldCalculateAvgMeetingDuration() {
        var avg = speechService.calculateAvgDurationPerMeeting();
        assertThat(avg).isEqualToIgnoringNanos(LocalTime.of(0, 8, 6));
    }

    @Test
    @DisplayName("должен вернуть среднее время, затрачиваемое сотрудником на встречу")
    void shouldCalculateAvgMeetingDurationByPerson() {
        var avg = speechService.calculateAvgDurationPerMeeting(PERSON0);
        assertThat(avg).isEqualToIgnoringNanos(LocalTime.of(0, 4, 10));
    }
}