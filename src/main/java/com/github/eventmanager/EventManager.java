package com.github.eventmanager;

import com.github.eventmanager.filehandlers.ConfigLoader;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.formatters.EventFormatter;
import com.github.eventmanager.formatters.KeyValueWrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * The EventManager class is responsible for managing and logging events.
 * It provides methods to log messages with different log levels and formats.
 */
public class EventManager {
    @Setter
    @Getter
    private String timeFormat;
    private LogHandler logHandler;

    /**
     * Constructs an EventManager with the specified LogHandler.
     *
     * @param logHandler the LogHandler to use for logging events.
     */
    public EventManager(LogHandler logHandler) {
        this.logHandler = logHandler;
        this.timeFormat = this.logHandler.getConfig().getEvent().getTimeFormat();
    }

    /**
     * Constructs an EventManager with the specified configuration file path.
     *
     * @param configPath the path to the configuration file.
     */
    public EventManager(String configPath) {
        this.logHandler = new LogHandler(configPath);
        this.timeFormat = this.logHandler.getConfig().getEvent().getTimeFormat();
    }

    /**
     * Sets the correct OS-specific file separator in the given path.
     *
     * @param path the file path to adjust.
     * @return the adjusted file path with the correct OS-specific separator.
     */
    public static String setCorrectOSSeperator(String path) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = path.replace("/", "\\");
        }
        return path;
    }

    /**
     * Logs a message to the destination file.
     *
     * @param level   the log level of the message.
     * @param message the object to log.
     * @throws IOException on input error.
     */
    private void logMessage(String level, Object message) {
        if (message instanceof Exception) {
            message = ((Exception) message).getMessage();
        } else {
            message = message.toString();
        }

        Map<String, String> metaData = setMetaDataFields(level);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        String event = switch (eventFormat) {
            case "kv" -> EventFormatter.KEY_VALUE.format(metaData, message.toString());
            case "csv" -> EventFormatter.CSV.format(metaData, message.toString());
            case "xml" -> EventFormatter.XML.format(metaData, message.toString());
            case "json" -> EventFormatter.JSON.format(metaData, message.toString());
            default -> EventFormatter.DEFAULT.format(metaData, message.toString());
        };

        writeEventToLogFile(event);
    }

    /**
     * Logs a message to the destination file.
     *
     * @param level    the log level of the message.
     * @param messages an object array to be appended to the message.
     * @throws IOException on input error.
     */
    private void logMessage(String level, KeyValueWrapper... messages) {
        Map<String, String> metaData = setMetaDataFields(level);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        String event = switch (eventFormat) {
            case "kv" -> EventFormatter.KEY_VALUE.format(metaData, messages);
            case "csv" -> EventFormatter.CSV.format(metaData, messages);
            case "xml" -> EventFormatter.XML.format(metaData, messages);
            case "json" -> EventFormatter.JSON.format(metaData, messages);
            default -> EventFormatter.DEFAULT.format(metaData, messages);
        };

        writeEventToLogFile(event);
    }

    /**
     * Writes the event to the log file.
     *
     * @param event the event to write.
     */
    private void writeEventToLogFile(String event) {
        if (this.logHandler.getConfig().getEvent().getPrintToConsole()) {
            System.out.print(event);
            return;
        } else if (this.logHandler.getConfig().getEvent().getPrintAndSaveToFile()) {
            System.out.print(event);
        }

        try {
            if (!this.logHandler.checkIfLogFileExists()) {
                this.logHandler.createLogFile();
            }
            String filePath = this.logHandler.getConfig().getLogFile().getFilePath();
            FileWriter myWriter = new FileWriter(filePath + this.logHandler.getCurrentFileName(), true);
            myWriter.write(event);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
    }

    /**
     * Sets the metadata fields for the event.
     *
     * @param level the log level of the event.
     * @return a map containing the metadata fields.
     */
    private Map<String, String> setMetaDataFields(String level) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String className = stackTraceElement[4].getClassName();
        String methodName = stackTraceElement[4].getMethodName();
        int lineNumber = stackTraceElement[4].getLineNumber();

        String time = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(this.timeFormat));

        Map<String, String> metaDataFields = new HashMap<>();
        metaDataFields.put("time", time);
        metaDataFields.put("level", level);
        metaDataFields.put("className", className);
        metaDataFields.put("methodName", methodName);
        metaDataFields.put("lineNumber", String.valueOf(lineNumber));

        return metaDataFields;
    }

    /**
     * Logs a fatal message.
     *
     * @param exception the exception to log.
     */
    public void logFatalMessage(Object exception) {
        logMessage("FATAL", exception);
    }

    /**
     * Logs a fatal message with key-value pairs.
     *
     * @param args the key-value pairs to log.
     */
    public void logFatalMessage(KeyValueWrapper... args) {
        logMessage("FATAL", args);
    }

    /**
     * Logs an error message.
     *
     * @param exception the exception to log.
     */
    public void logErrorMessage(Object exception) {
        logMessage("ERROR", exception);
    }

    /**
     * Logs an error message with key-value pairs.
     *
     * @param args the key-value pairs to log.
     */
    public void logErrorMessage(KeyValueWrapper... args) {
        logMessage("ERROR", args);
    }

    /**
     * Logs a warning message.
     *
     * @param exception the exception to log.
     */
    public void logWarningMessage(Object exception) {
        logMessage("WARNING", exception);
    }

    /**
     * Logs a warning message with key-value pairs.
     *
     * @param args the key-value pairs to log.
     */
    public void logWarningMessage(KeyValueWrapper... args) {
        logMessage("WARNING", args);
    }

    /**
     * Checks if informational logs are enabled.
     *
     * @return true if informational logs are enabled, false otherwise.
     */
    public boolean areInfoLogsEnabled() {
        boolean informationalMode = this.logHandler.getConfig().getEvent().getInformationalMode();
        boolean debuggingMode = this.logHandler.getConfig().getEvent().getDebuggingMode();
        return informationalMode || debuggingMode;
    }

    /**
     * Logs an informational message.
     *
     * @param exception the exception to log.
     */
    public void logInfoMessage(Object exception) {
        if (areInfoLogsEnabled()) {
            logMessage("INFO", exception);
        }
    }

    /**
     * Logs an informational message with key-value pairs.
     *
     * @param args the key-value pairs to log.
     */
    public void logInfoMessage(KeyValueWrapper... args) {
        if (areInfoLogsEnabled()) {
            logMessage("INFO", args);
        }
    }

    /**
     * Logs a debug message.
     *
     * @param exception the exception to log.
     */
    public void logDebugMessage(Object exception) {
        if (this.logHandler.getConfig().getEvent().getDebuggingMode()) {
            logMessage("DEBUG", exception);
        }
    }

    /**
     * Logs a debug message with key-value pairs.
     *
     * @param args the key-value pairs to log.
     */
    public void logDebugMessage(KeyValueWrapper... args) {
        if (this.logHandler.getConfig().getEvent().getDebuggingMode()) {
            logMessage("DEBUG", args);
        }
    }
}