package com.dev.fr13.message;

import com.dev.fr13.domain.Person;
import com.dev.fr13.domain.Speech;
import com.dev.fr13.persistence.service.SpeechService;
import com.dev.fr13.skype.domain.MostValuableSpeech;
import org.springframework.context.MessageSource;

public class CommonStatMessage extends AbstractMessageFormatter {
    private static final String GREET = "stat.greet";
    private static final String TOTAL_MEETINGS = "stat.total-meetings";
    private static final String TOTAL_DURATION = "stat.total-duration";
    private static final String AVG_DURATION = "stat.avg-duration";
    private static final String MAX_DURATION = "stat.max-duration";
    private static final String MIN_DURATION = "stat.min-duration";
    private static final String MOST_ACCURATE = "stat.most-accurate";
    private static final String MOST_INACCURATE = "stat.most-inaccurate";

    private final StringBuilder builder = new StringBuilder();

    public CommonStatMessage(SpeechService speechService, MessageSource messageSource) {
        super(speechService, messageSource);
    }

    @Override
    public String prepareMessage() {
        return initMessage()
                .withTotalMeetingsNumber()
                .withTotalSpeechDuration()
                .withAvgDurationPerMeeting()
                .withMaxSpeechDuration()
                .withMinSpeechDuration()
                .withMostAccurateSpeech()
                .withMostInaccurateSpeech()
                .build();
    }

    private CommonStatMessage initMessage() {
        builder.append(getSourceMessage(GREET));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withTotalMeetingsNumber() {
        var totalMeetings = speechService.calculateTotalMeetingsNumber();
        var templateTotalMeetings = getSourceMessage(TOTAL_MEETINGS);
        builder.append(String.format(templateTotalMeetings, totalMeetings));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withTotalSpeechDuration() {
        var templateTotalDuration = getSourceMessage(TOTAL_DURATION);
        var totalDuration = speechService.calculateTotalSpeechDuration();
        var formattedTotalDuration = totalDuration.minusDays(1).format(DAYS_HOURS_MINUTES);
        builder.append(String.format(templateTotalDuration, formattedTotalDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withAvgDurationPerMeeting() {
        var templateAvgDuration = getSourceMessage(AVG_DURATION);
        var avgDuration = speechService.calculateAvgDurationPerMeeting();
        var formattedAvgDuration = avgDuration.format(
                avgDuration.isAfter(HOUR) ? HOURS_MINUTES : MINUTES_SECONDS);
        builder.append(String.format(templateAvgDuration, formattedAvgDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withMaxSpeechDuration() {
        var templateMaxDuration = getSourceMessage(MAX_DURATION);
        var maxDuration = speechService.findSpeechWithMaxDuration()
                .orElse(Speech.getDefaultSpeech());
        var formattedMaxDuration = maxDuration.getDuration().format(MINUTES_SECONDS);
        builder.append(String.format(templateMaxDuration, maxDuration.getPersonName(), formattedMaxDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withMinSpeechDuration() {
        var templateMinDuration = getSourceMessage(MIN_DURATION);
        var minDuration = speechService.findSpeechWithMinDuration()
                .orElse(Speech.getDefaultSpeech());
        var formattedMinDuration = minDuration.getDuration().format(
                minDuration.getDuration().isAfter(MINUTE) ? MINUTES_SECONDS : SECONDS);
        builder.append(String.format(templateMinDuration, minDuration.getPersonName(), formattedMinDuration));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withMostAccurateSpeech() {
        var templateMostAccurate = getSourceMessage(MOST_ACCURATE);
        var mostAccurate = speechService.findMostAccurateSpeech()
                .orElse(new MostValuableSpeech(Person.getDefaultPerson(), 0.0));
        builder.append(String.format(templateMostAccurate, mostAccurate.getPersonName(), mostAccurate.getPercents()));
        builder.append(BREAK_LINE);
        return this;
    }

    private CommonStatMessage withMostInaccurateSpeech() {
        var templateMostInaccurate = getSourceMessage(MOST_INACCURATE);
        var mostInaccurate = speechService.findMostInaccurateSpeech()
                .orElse(new MostValuableSpeech(Person.getDefaultPerson(), 0.0));
        builder.append(String.format(templateMostInaccurate, mostInaccurate.getPersonName(), mostInaccurate.getPercents()));
        return this;
    }

    private String build() {
        return builder.toString();
    }
}