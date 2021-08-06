package com.dev.fr13.skype.exception;

public class FailedGetCredential extends RuntimeException {

    public FailedGetCredential(int status, String desc) {
        super(String.format("Fail to get credential. Status code: %s, error desc %s.", status, desc));
    }

    public FailedGetCredential(Exception e) {
        super(e);
    }
}
