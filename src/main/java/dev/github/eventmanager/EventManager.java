package dev.github.eventmanager;

import dev.github.eventmanager.filehandlers.ConfigLoader;
import dev.github.eventmanager.filehandlers.LogHandler;
import dev.github.eventmanager.formatters.EventFormatter;
import dev.github.eventmanager.formatters.KeyValueWrapper;
import jdk.jfr.Event;
import lombok.Getter;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public EventManager() {
        this.logHandler = new LogHandler(new ConfigLoader());
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
     * @param message The message to log.
     * @throws IOException On input error.
     */
    private void logMessage(String level, Object message) {
        if (message instanceof Exception) {
            message = ((Exception) message).getMessage();
        } else {
            message = message.toString();
        }

        Map<String, String> metaData = setMetaDataFields(level);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        System.out.println("eventFormat: " + eventFormat);

        String event = switch (eventFormat) {
            case "kv" ->
                    EventFormatter.KEY_VALUE.format(metaData, new KeyValueWrapper("exception", message.toString()));
            default -> EventFormatter.DEFAULT.format(metaData, new KeyValueWrapper("exception", message.toString()));
        };

        if (this.logHandler.getConfig().getEvent().isPrintToConsole()) {
            System.out.print(event);
            return;
        } else if (this.logHandler.getConfig().getEvent().isPrintAndSaveToFile()) {
            System.out.print(event);
        }

        writeEventToLogFile(event);
    }

    private void writeEventToLogFile(String event) {
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
        String callerClassName = stackTraceElement[4].getClassName();
        String callerMethodName = stackTraceElement[4].getMethodName();
        int lineNumber = stackTraceElement[4].getLineNumber();

        // Get the current time as a string in the specified format
        String time = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(this.timeFormat));

        Map<String, String> metaDataFields = new HashMap<>();
        metaDataFields.put("time", time);
        metaDataFields.put("level", level);
        metaDataFields.put("callerClassName", callerClassName);
        metaDataFields.put("callerMethodName", callerMethodName);
        metaDataFields.put("lineNumber", String.valueOf(lineNumber));

        return metaDataFields;
    }

    public void logFatalMessage(Object exception) {
        logMessage("FATAL", exception);
    }

    public void logErrorMessage(Object exception) {
        logMessage("ERROR", exception);
    }

    public void logWarningMessage(Object exception) {
        logMessage("WARNING", exception);
    }

    public void logInfoMessage(Object exception) {
        boolean informationalMode = this.logHandler.getConfig().getEvent().isInformationalMode();
        boolean debuggingMode = this.logHandler.getConfig().getEvent().isDebuggingMode();
        if (informationalMode || debuggingMode) {
            logMessage("INFO", exception);
        }
    }

    public void logDebugMessage(Object exception) {
        if (this.logHandler.getConfig().getEvent().isDebuggingMode()) {
            logMessage("DEBUG", exception);
        }
    }
}
