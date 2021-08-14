package com.dev.fr13.config;

import com.dev.fr13.google.GoogleCredential;
import com.dev.fr13.google.GoogleSheetsClient;
import com.dev.fr13.persistence.service.SpeechService;
import com.dev.fr13.service.MeetingUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleSheetsConfig {
    @Value("${google.spreadsheet-id}")
    private String spreadSheetId;

    @Value("${google.cells-range}")
    private String cellsRange;

    @Bean
    public GoogleSheetsClient googleSheetsClient(GoogleCredential credential) {
        return new GoogleSheetsClient(credential.getCredential(), spreadSheetId);
    }

    @Bean
    public MeetingUpdate meetingUpdate(SpeechService speechService, GoogleSheetsClient sheetsClient) {
        return new MeetingUpdate(speechService, sheetsClient, cellsRange);
    }
}