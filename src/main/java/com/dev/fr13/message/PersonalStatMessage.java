package com.dev.fr13.message;

import com.dev.fr13.domain.Person;
import com.dev.fr13.domain.Speech;
import com.dev.fr13.persistence.service.SpeechService;
import com.dev.fr13.skype.domain.MostValuableSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

public class PersonalStatMessage extends AbstractMessageFormatter {
    private static final Logger log = LoggerFactory.getLogger(PersonalStatMessage.class);

    private static final String GREET = "personal-stat.greet";
    private static final String TOTAL_MEETINGS = "personal-stat.total-meetings";
    private static final String TOTAL_SKIPPED_MEETINGS = "personal-stat.total-meetings-skipped";
    private static final String TOTAL_DURATION = "personal-stat.total-duration";
    private static final String MAX_DURATION = "personal-stat.max-duration";
    private static final String MIN_DURATION = "personal-stat.min-duration";
    private static final String AVG_DURATION = "personal-stat.avg-duration";
    private static final String ACCURATE = "personal-stat.accurate";
    private static final String WRONG_ARGS = "personal-stat.wrong-args";

    private final Person person;
    private final StringBuilder builder = new StringBuilder();

    public PersonalStatMessage(SpeechService speechService, MessageSource messageSource, Person person) {
        super(speechService, messageSource);
        this.person = person;
    }

    @Override
    public String prepareMessage() {
        if (person.equals(Person.getDefaultPerson())) {
            log.info("Wrong number of args");
            return getSourceMessage(WRONG_ARGS);
        }

        var optPerson = speechService.findPerson(person);
        if (optPerson.isEmpty()) {
            log.info("Unknown person {}", person);
            return initMessage().build();
        }

        log.info("Prepare message for {}", person);
        return initMessage()
                .withTotalMeetingsNumber()
                .withSkippedMeetingsNumber()
                .withTotalSpeechDuration()
                .withMaxSpeechDuration()
                .withMinSpeechDuration()
                .withAvgDurationPerMeeting()
                .withPercentAccurateMeetings()
                .build();
    }

    private PersonalStatMessage initMessage() {
        var templatePerson = getSourceMessage(GREET);
        builder.append(String.format(templatePerson, person.getName()));
        builder.append(BREAK_LINE);
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withTotalMeetingsNumber() {
        var totalMeetings = speechService.calculateTotalMeetingsNumber(person);
        var templateTotalMeetings = getSourceMessage(TOTAL_MEETINGS);
        builder.append(String.format(templateTotalMeetings, totalMeetings));
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withSkippedMeetingsNumber() {
        var totalSkippedMeetings = speechService.calculateSkippedMeetingsNumberByPerson(person);
        var templateTotalSkippedMeetings = getSourceMessage(TOTAL_SKIPPED_MEETINGS);
        builder.append(String.format(templateTotalSkippedMeetings, totalSkippedMeetings));
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withTotalSpeechDuration() {
        var totalDurationMeetings = speechService.calculateTotalSpeechDuration(person);
        var templateTotalDurationMeetings = getSourceMessage(TOTAL_DURATION);
        var formattedTotalDuration = totalDurationMeetings.format(HOURS_MINUTES);
        builder.append(String.format(templateTotalDurationMeetings, formattedTotalDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withMaxSpeechDuration() {
        var maxDuration = speechService.findSpeechWithMaxDuration(person)
                .orElseGet(Speech::getDefaultSpeech);
        var templateMaxDuration = getSourceMessage(MAX_DURATION);
        var formattedMaxDate = maxDuration.getDate().format(DAY_MOTH_YEAR);
        var formattedMaxDuration = maxDuration.getDuration().format(MINUTES_SECONDS);
        builder.append(String.format(templateMaxDuration, formattedMaxDate, formattedMaxDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withMinSpeechDuration() {
        var minDuration = speechService.findSpeechWithMinDuration(person)
                .orElseGet(Speech::getDefaultSpeech);
        var templateMinDuration = getSourceMessage(MIN_DURATION);
        var formattedMinDate = minDuration.getDate().format(DAY_MOTH_YEAR);
        var formattedMinDuration = minDuration.getDuration().format(
                minDuration.getDuration().isAfter(MINUTE) ? MINUTES_SECONDS : SECONDS);
        builder.append(String.format(templateMinDuration, formattedMinDate, formattedMinDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withAvgDurationPerMeeting() {
        var avgDuration = speechService.calculateAvgDurationPerMeeting(person);
        var templateAvgDuration = getSourceMessage(AVG_DURATION);
        var formattedAvgDuration = avgDuration.format(MINUTES_SECONDS);
        builder.append(String.format(templateAvgDuration, formattedAvgDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private PersonalStatMessage withPercentAccurateMeetings() {
        var percentAccurateMeetings = speechService.calculatePercentAccurateMeetings(person)
                .map(MostValuableSpeech::getPercents)
                .orElse(0.0);
        var templateAccurate = getSourceMessage(ACCURATE);
        builder.append(String.format(templateAccurate, percentAccurateMeetings));
        return this;
    }

    private String build() {
        return builder.toString();
    }
}
