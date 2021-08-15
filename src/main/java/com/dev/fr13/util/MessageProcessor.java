package com.dev.fr13.util;

import java.util.Optional;

public class MessageProcessor {
    private static final String COMMAND_ID_STAT = "/stat";
    private static final String COMMAND_ID_ABOUT = "/about";

    private static final String SPLITTER = " ";
    private static final int EXPECTED_LENGTH = 3;

    public static Optional<String> extractPersonName(String msg) {
        var parts = msg.split(SPLITTER);
        if (parts.length == EXPECTED_LENGTH) {
            return Optional.of(parts[1] + SPLITTER + parts[2]);
        }
        return Optional.empty();
    }

    public static boolean isCommonStat(String msg) {
        return msg.startsWith(COMMAND_ID_STAT)
                && msg.strip().length() == COMMAND_ID_STAT.length();
    }

    public static boolean isPersonalStat(String msg) {
        return msg.startsWith(COMMAND_ID_STAT);
    }

    public static boolean isAbout(String msg) {
        return msg.startsWith(COMMAND_ID_ABOUT);
    }

    private MessageProcessor() {
        throw new UnsupportedOperationException();
    }
}