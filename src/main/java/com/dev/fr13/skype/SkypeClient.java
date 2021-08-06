package com.dev.fr13.skype;

import com.dev.fr13.skype.domain.SkypeMessage;
import com.dev.fr13.skype.exception.FailedSendMessage;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkypeClient {
    private static final Logger log = LoggerFactory.getLogger(SkypeClient.class);

    private static final String URL = "https://smba.trafficmanager.net/apis/v3/conversations/{conversationId}/activities";
    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String CONVERSATION_ID = "conversationId";

    public static void sendMessage(String token, String conversationId, String text) {
        try {
            log.debug("Try to send msg to chat {}", conversationId);
            Unirest.post(URL)
                    .header(AUTH_HEADER_KEY, BEARER + token)
                    .routeParam(CONVERSATION_ID, conversationId)
                    .body(new SkypeMessage(conversationId, text))
                    .asJson()
                    .ifSuccess(r -> log.info("Message was send successfully"))
                    .ifFailure(r -> {
                        var json = r.getBody().getObject();
                        throw new FailedSendMessage(r.getStatus(), json.getString("error"));
                    });
        } catch (UnirestException e) {
            throw new FailedSendMessage(e);
        }
    }

    private SkypeClient() {
        throw new UnsupportedOperationException();
    }
}
