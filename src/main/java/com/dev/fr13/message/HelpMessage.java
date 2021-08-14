package com.dev.fr13.message;

import com.dev.fr13.persistence.service.SpeechService;
import org.springframework.context.MessageSource;

public class HelpMessage extends AbstractMessageFormatter {
    private static final String HELP_GREET = "help.greet";
    private static final String HELP_POLICY = "help.policy";
    private static final String HELP_ABILITY_COMMON = "help.ability-common";
    private static final String HELP_ABILITY_PERSON = "help.ability-person";

    protected HelpMessage(SpeechService speechService, MessageSource messageSource) {
        super(speechService, messageSource);
    }

    @Override
    public String prepareMessage() {
        return getSourceMessage(HELP_GREET) +
                BREAK_LINE +
                getSourceMessage(HELP_POLICY) +
                BREAK_LINE +
                getSourceMessage(HELP_ABILITY_COMMON) +
                BREAK_LINE +
                getSourceMessage(HELP_ABILITY_PERSON);
    }
}