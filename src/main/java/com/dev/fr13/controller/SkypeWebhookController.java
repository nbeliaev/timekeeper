package com.dev.fr13.controller;

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

    public SkypeWebhookController(SkypeCredential skypeCredential) {
        this.skypeCredential = skypeCredential;
    }

    @PostMapping("/")
    public ResponseEntity<String> handleBotEvent(@RequestBody SkypeMessage message) {
        log.debug("Process a new message from a chat {}", message.getConversationId());
        SkypeClient.sendMessage(skypeCredential.getToken(), message.getConversationId(), message.getText());
        return ResponseEntity.ok().build();
    }
}