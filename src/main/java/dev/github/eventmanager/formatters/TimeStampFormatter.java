package dev.github.eventmanager.formatters;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimeStampFormatter {
    private String timeFormat;
    public List<String> availableTimeFormats;
    public static boolean isValidTimeFormat(String timeFormat) {
        try {
            DateTimeFormatter.ofPattern(timeFormat);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
