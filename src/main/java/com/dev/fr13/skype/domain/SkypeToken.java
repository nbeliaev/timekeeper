package com.dev.fr13.skype.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SkypeToken {
    private static final Logger log = LoggerFactory.getLogger(SkypeToken.class);

    private final String token;
    private final LocalDateTime expiryDate;

    public SkypeToken(String token, int expiresIn) {
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusSeconds(expiresIn);
        log.debug("Token has been cached until {}", this.expiryDate);
    }

    public String get() {
        log.debug("Token was gotten from cache");
        return token;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiryDate);
    }
}