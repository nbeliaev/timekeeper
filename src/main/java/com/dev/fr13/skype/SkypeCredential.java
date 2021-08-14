package com.dev.fr13.skype;

import com.dev.fr13.skype.domain.SkypeToken;
import com.dev.fr13.skype.exception.FailedGetCredential;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class SkypeCredential {
    private static final String URL = "https://login.microsoftonline.com/botframework.com/oauth2/v2.0/token";
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String GRANT_TYPE_VALUE = "client_credentials";
    private static final String SCOPE_KEY = "scope";
    private static final String SCOPE_VALUE = "https://api.botframework.com/.default";

    private final String appId;
    private final String password;

    private SkypeToken skypeToken;

    public SkypeCredential(String appId, String password) {
        this.appId = appId;
        this.password = password;
    }

    public synchronized String getToken() {
        if (skypeToken != null && skypeToken.isValid()) {
            return skypeToken.get();
        }

        try {
            Unirest.post(URL)
                    .field(GRANT_TYPE_KEY, GRANT_TYPE_VALUE)
                    .field(CLIENT_ID_KEY, appId)
                    .field(CLIENT_SECRET_KEY, password)
                    .field(SCOPE_KEY, SCOPE_VALUE)
                    .asJson()
                    .ifSuccess(resp -> {
                        var json = resp.getBody().getObject();
                        skypeToken = new SkypeToken(json.getString("access_token"), json.getInt("expires_in"));
                    })
                    .ifFailure(resp -> {
                        var json = resp.getBody().getObject();
                        throw new FailedGetCredential(resp.getStatus(), json.getString("error"));
                    });
        } catch (UnirestException e) {
            throw new FailedGetCredential(e);
        }

        return skypeToken.get();
    }
}
