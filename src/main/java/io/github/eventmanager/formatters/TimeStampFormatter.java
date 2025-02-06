package io.github.eventmanager.formatters;

import java.time.format.DateTimeFormatter;

public class TimeStampFormatter {
    public static boolean isValidTimeFormat(String timeFormat) {
        try {
            DateTimeFormatter.ofPattern(timeFormat);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
