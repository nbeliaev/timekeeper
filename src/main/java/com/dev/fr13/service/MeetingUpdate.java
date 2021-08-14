package com.dev.fr13.service;

import com.dev.fr13.domain.Person;
import com.dev.fr13.domain.Speech;
import com.dev.fr13.google.GoogleSheetsClient;
import com.dev.fr13.persistence.service.SpeechService;
import com.dev.fr13.util.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MeetingUpdate {
    private static final Logger log = LoggerFactory.getLogger(MeetingUpdate.class);

    private static final String RANGE_TEMPLATE = "%s!%s";
    private static final int NAME_CELL_IDX = 0;
    private static final int DURATION_CELL_IDX = 7;

    private final SpeechService speechService;
    private final GoogleSheetsClient sheetsClient;
    private final String cellsRange;

    public MeetingUpdate(SpeechService speechService, GoogleSheetsClient sheetsClient, String cellsRange) {
        this.speechService = speechService;
        this.sheetsClient = sheetsClient;
        this.cellsRange = cellsRange;
    }

    @Scheduled(cron = "${meeting.update-schedule}")
    public void update() {
        var titles = sheetsClient.getSheetsTitles();
        titles.forEach(title -> {
            var range = String.format(RANGE_TEMPLATE, title, cellsRange);
            var sheetData = sheetsClient.fetchRangeData(range);
            if (sheetData == null || sheetData.isEmpty()) {
                log.warn("No data on tab {}", title);
            } else {
                var speeches = collectSpeeches(sheetData, DateTimeConverter.stringToDate(title));
                speechService.saveAll(speeches);
            }
        });
    }

    private List<Speech> collectSpeeches(List<List<Object>> sheetData, LocalDate date) {
        var result = new ArrayList<Speech>();
        for (var rowData : sheetData) {
            if (!rowData.isEmpty()) {
                var name = (String) rowData.get(NAME_CELL_IDX);
                var person = new Person(name);
                if (isDurationAvailable(rowData)) {
                    var duration = DateTimeConverter.stringToTime((String) rowData.get(DURATION_CELL_IDX));
                    var speech = new Speech(date, person, duration);
                    result.add(speech);
                    log.info("{} has been fetched", speech);
                } else {
                    var speech = new Speech(date, person);
                    result.add(speech);
                    log.info("{} has been fetched", speech);
                }
            }
        }
        return result;
    }

    private boolean isDurationAvailable(List<Object> rowData) {
        return rowData.size() == 8;
    }
}