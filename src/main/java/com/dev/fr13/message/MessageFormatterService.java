package com.dev.fr13.message;

import com.dev.fr13.domain.Person;
import com.dev.fr13.persistence.service.SpeechService;
import com.dev.fr13.util.MessageProcessor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageFormatterService {
    private final SpeechService speechService;
    private final MessageSource messageSource;

    public MessageFormatterService(SpeechService speechService, MessageSource messageSource) {
        this.speechService = speechService;
        this.messageSource = messageSource;
    }

    public String prepareAnswer(String incomingMsg) {
        MessageFormatter messageFormatter;
        if (MessageProcessor.isCommonStat(incomingMsg)) {
            messageFormatter = new CommonStatMessage(speechService, messageSource);
        } else if (MessageProcessor.isPersonalStat(incomingMsg)) {
            messageFormatter = MessageProcessor.extractPersonName(incomingMsg)
                    .map(s -> new PersonalStatMessage(speechService, messageSource, new Person(s)))
                    .orElseGet(() -> new PersonalStatMessage(speechService, messageSource, Person.getDefaultPerson()));
        } else if (MessageProcessor.isAbout(incomingMsg)) {
            messageFormatter = new AboutMessage(speechService, messageSource);
        } else {
            messageFormatter = new HelpMessage(speechService, messageSource);
        }
        return messageFormatter.prepareMessage();
    }
}