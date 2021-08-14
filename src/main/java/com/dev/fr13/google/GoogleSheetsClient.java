package com.dev.fr13.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleSheetsClient {
    private static final Logger log = LoggerFactory.getLogger(GoogleSheetsClient.class);

    private static final String APPLICATION_NAME = "Timekeeper";
    private final Credential credential;
    private final String spreadsheetId;

    public GoogleSheetsClient(Credential credential, String spreadsheetId) {
        this.credential = credential;
        this.spreadsheetId = spreadsheetId;
    }

    public List<String> getSheetsTitles() {
        var sheets = getSheets();
        return sheets.stream()
                .map(i -> i.getProperties().getTitle())
                .collect(Collectors.toUnmodifiableList());
    }

    public List<List<Object>> fetchRangeData(String range) {
        log.info("Fetch data from {}", range);
        var sheets = createSheets();
        try {
            var resp = sheets.spreadsheets()
                    .values()
                    .get(spreadsheetId, range)
                    .execute();
            var res = resp.getValues();
            if (res != null) {
                log.info("Found {} rows", res.size());
            }
            return res;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Sheet> getSheets() {
        var sheets = createSheets();
        try {
            var spreadsheet = sheets.spreadsheets().get(spreadsheetId).execute();
            return spreadsheet.getSheets();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Sheets createSheets() {
        try {
            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
