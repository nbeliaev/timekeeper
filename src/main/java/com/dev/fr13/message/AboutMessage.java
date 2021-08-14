package com.dev.fr13.message;

import com.dev.fr13.persistence.service.SpeechService;
import org.springframework.context.MessageSource;

public class AboutMessage extends AbstractMessageFormatter {
    private static final String ABOUT_POWERED = "about.powered";
    private static final String ABOUT_SOURCE = "about.source";
    private static final String ABOUT_PROCESSING = "about.processing";

    public AboutMessage(SpeechService speechService, MessageSource messageSource) {
        super(speechService, messageSource);
    }

    @Override
    public String prepareMessage() {
        return getSourceMessage(ABOUT_POWERED) +
                BREAK_LINE +
                getSourceMessage(ABOUT_SOURCE) +
                BREAK_LINE +
                getSourceMessage(ABOUT_PROCESSING);
    }
}