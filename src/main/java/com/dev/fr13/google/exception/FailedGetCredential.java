package com.dev.fr13.google.exception;

public class FailedGetCredential extends RuntimeException {

    public FailedGetCredential(Throwable cause) {
        super("Couldn't get Google credential", cause);
    }
}