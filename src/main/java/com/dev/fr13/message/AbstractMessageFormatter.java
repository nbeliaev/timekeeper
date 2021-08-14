package com.dev.fr13.message;

import com.dev.fr13.persistence.service.SpeechService;
import org.springframework.context.MessageSource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class AbstractMessageFormatter implements MessageFormatter {
    protected final SpeechService speechService;
    private final MessageSource messageSource;

    protected static final DateTimeFormatter DAY_MOTH_YEAR = DateTimeFormatter.ofPattern("d.MM.yy");
    protected static final DateTimeFormatter DAYS_HOURS_MINUTES = DateTimeFormatter.ofPattern("d'д.' HH'ч.' mm'м.'");
    protected static final DateTimeFormatter HOURS_MINUTES = DateTimeFormatter.ofPattern("HH'ч.' mm'м.'");
    protected static final DateTimeFormatter MINUTES_SECONDS = DateTimeFormatter.ofPattern("mm'м.' ss'сек.'");
    protected static final DateTimeFormatter SECONDS = DateTimeFormatter.ofPattern("ss'сек.'");

    protected static final LocalTime MINUTE = LocalTime.of(0, 1, 0);
    protected static final LocalTime HOUR = LocalTime.of(1, 0, 0);

    protected static final String BREAK_LINE = "\n";

    protected AbstractMessageFormatter(SpeechService speechService, MessageSource messageSource) {
        this.speechService = speechService;
        this.messageSource = messageSource;
    }

    protected String getSourceMessage(String s) {
        return messageSource.getMessage(s, null, Locale.getDefault());
    }
}