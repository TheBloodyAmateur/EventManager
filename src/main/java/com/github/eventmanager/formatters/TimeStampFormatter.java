package com.github.eventmanager.formatters;

import java.time.format.DateTimeFormatter;

/**
 * The TimeStampFormatter class provides utility methods for validating time formats.
 */
public class TimeStampFormatter {

    /**
     * Checks if the provided time format is valid.
     *
     * @param timeFormat the time format to validate.
     * @return true if the time format is valid, false otherwise.
     */
    public static boolean isValidTimeFormat(String timeFormat) {
        try {
            DateTimeFormatter.ofPattern(timeFormat);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}