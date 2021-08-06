package com.dev.fr13.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleCredential {

    public static Credential getCredential() throws IOException, GeneralSecurityException {
        //var in = TimeKeeper.class.getResourceAsStream("/client_secret.json");
        var in = GoogleCredential.class.getResourceAsStream("/client_secret.json");
        if (in == null) {
            throw new IllegalStateException("Couldn't load Google client secret file");
        }
        var clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(),
                new BufferedReader(new InputStreamReader(in))
        );

        var flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                clientSecrets,
                List.of(SheetsScopes.SPREADSHEETS)
        ).setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}
