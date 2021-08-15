package com.dev.fr13.controller;

import com.dev.fr13.message.MessageFormatterService;
import com.dev.fr13.skype.SkypeClient;
import com.dev.fr13.skype.SkypeCredential;
import com.dev.fr13.skype.domain.SkypeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkypeWebhookController {
    private static final Logger log = LoggerFactory.getLogger(SkypeWebhookController.class);

    private final SkypeCredential skypeCredential;
    private final MessageFormatterService messageFormatterService;

    public SkypeWebhookController(SkypeCredential skypeCredential, MessageFormatterService messageFormatterService) {
        this.skypeCredential = skypeCredential;
        this.messageFormatterService = messageFormatterService;
    }

    @PostMapping("/")
    public ResponseEntity<String> handleBotEvent(@RequestBody SkypeMessage message) {
        log.debug("Process a new message from a chat {}", message.getConversationId());
        var text = message.getText();
        if (text != null) {
            var answer = messageFormatterService.prepareAnswer(text);
            SkypeClient.sendMessage(skypeCredential.getToken(), message.getConversationId(), answer);
        }
        return ResponseEntity.ok().build();
    }
}