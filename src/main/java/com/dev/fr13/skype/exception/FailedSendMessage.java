package com.dev.fr13.skype.exception;

public class FailedSendMessage extends RuntimeException {

    public FailedSendMessage(int status, String desc) {
        super(String.format("Fail to send message. Status code: %s, error desc %s.", status, desc));
    }

    public FailedSendMessage(Throwable cause) {
        super(cause);
    }
}
