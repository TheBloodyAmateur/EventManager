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

public class EventManager {
    @Setter
    @Getter
    private String timeFormat;
    private LogHandler logHandler;

    public EventManager(LogHandler logHandler) {
        this.logHandler = logHandler;
        this.timeFormat = this.logHandler.getConfig().getEvent().getTimeFormat();
    }

    public EventManager(String configPath) {
        this.logHandler = new LogHandler(new ConfigLoader(configPath));
        this.timeFormat = this.logHandler.getConfig().getEvent().getTimeFormat();
    }

    public static String setCorrectOSSeperator(String path) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = path.replace("/", "\\");
        }
        return path;
    }

    /**
     * Log a message to the destination file.
     *
     * @param level   The log level of the message.
     * @param message The object to log.
     * @throws IOException On input error.
     */
    private void logMessage(String level, Object message) {
        if (message instanceof Exception) {
            message = ((Exception) message).getMessage();
        } else {
            message = message.toString();
        }

        // Set the metadata fields and get the event format
        Map<String, String> metaData = setMetaDataFields(level);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        String event = switch (eventFormat) {
            case "kv" ->
                    EventFormatter.KEY_VALUE.format(metaData, message.toString());
            case "csv" ->
                    EventFormatter.CSV.format(metaData, message.toString());
            case "xml" ->
                    EventFormatter.XML.format(metaData, message.toString());
            case "json" ->
                    EventFormatter.JSON.format(metaData, message.toString());
            default ->
                    EventFormatter.DEFAULT.format(metaData, message.toString());

        };

        writeEventToLogFile(event);
    }

    /**
     * Log a message to the destination file.
     *
     * @param level   The log level of the message.
     * @param messages An object array ot be appended to the message.
     * @throws IOException On input error.
     */
    private void logMessage(String level, KeyValueWrapper ...messages) {
        // Set the metadata fields and get the event format
        Map<String, String> metaData = setMetaDataFields(level);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        String event = switch (eventFormat) {
            case "kv" ->
                    EventFormatter.KEY_VALUE.format(metaData, messages);
            case "csv" ->
                    EventFormatter.CSV.format(metaData, messages);
            case "xml" ->
                    EventFormatter.XML.format(metaData, messages);
            case "json" ->
                    EventFormatter.JSON.format(metaData, messages);
            default ->
                    EventFormatter.DEFAULT.format(metaData, messages);

        };

        writeEventToLogFile(event);
    }

    private void writeEventToLogFile(String event) {
        if (this.logHandler.getConfig().getEvent().getPrintToConsole()) {
            System.out.print(event);
            return;
        } else if (this.logHandler.getConfig().getEvent().getPrintAndSaveToFile()) {
            System.out.print(event);
        }

        try {
            // Check if the file exists and create it if it doesn't
            if (!this.logHandler.checkIfLogFileExists()) {
                this.logHandler.createLogFile();
            }
            // Write the event to the file
            String filePath = this.logHandler.getConfig().getLogFile().getFilePath();
            FileWriter myWriter = new FileWriter(filePath + this.logHandler.getCurrentFileName(), true);
            myWriter.write(event);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
    }

    private Map<String, String> setMetaDataFields(String level) {
        // Get the class name and method of the caller
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String className = stackTraceElement[4].getClassName();
        String methodName = stackTraceElement[4].getMethodName();
        int lineNumber = stackTraceElement[4].getLineNumber();

        // Get the current time as a string in the specified format
        String time = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(this.timeFormat));

        Map<String, String> metaDataFields = new HashMap<>();
        metaDataFields.put("time", time);
        metaDataFields.put("level", level);
        metaDataFields.put("className", className);
        metaDataFields.put("methodName", methodName);
        metaDataFields.put("lineNumber", String.valueOf(lineNumber));

        return metaDataFields;
    }

    public void logFatalMessage(Object exception) {
        logMessage("FATAL", exception);
    }

    public void logFatalMessage(KeyValueWrapper ...args) {
        logMessage("FATAL", args);
    }

    public void logErrorMessage(Object exception) {
        logMessage("ERROR", exception);
    }

    public void logErrorMessage(KeyValueWrapper ...args) {
        logMessage("ERROR", args);
    }

    public void logWarningMessage(Object exception) {
        logMessage("WARNING", exception);
    }

    public void logWarningMessage(KeyValueWrapper ...args) {
        logMessage("WARNING", args);
    }

    public boolean areInfoLogsEnabled(){
        boolean informationalMode = this.logHandler.getConfig().getEvent().getInformationalMode();
        boolean debuggingMode = this.logHandler.getConfig().getEvent().getDebuggingMode();
        return informationalMode || debuggingMode;
    }

    public void logInfoMessage(Object exception) {
        if (areInfoLogsEnabled()) {
            logMessage("INFO", exception);
        }
    }

    public void logInfoMessage(KeyValueWrapper ...args) {
        if (areInfoLogsEnabled()) {
            logMessage("INFO", args);
        }
    }

    public void logDebugMessage(Object exception) {
        if (this.logHandler.getConfig().getEvent().getDebuggingMode()) {
            logMessage("DEBUG", exception);
        }
    }

    public void logDebugMessage(KeyValueWrapper ...args) {
        if (this.logHandler.getConfig().getEvent().getDebuggingMode()) {
            logMessage("DEBUG", args);
        }
    }
}
