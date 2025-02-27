package com.github.eventmanager;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The EventManager class is responsible for managing and logging events.
 * It provides methods to log messages with different log levels and formats.
 */
public class EventManager {
    @Setter
    @Getter
    private String timeFormat;
    @Getter
    private final LogHandler logHandler;
    private static final BlockingQueue<String> eventQueue = new LinkedBlockingQueue<>();
    private final Thread eventThread;

    /**
     * Constructs an EventManager with the specified LogHandler.
     *
     * @param logHandler the LogHandler to use for logging events.
     */
    public EventManager(LogHandler logHandler) {
        this.logHandler = logHandler;
        this.timeFormat = this.logHandler.getConfig().getEvent().getTimeFormat();
        this.eventThread = initiatEventThread();
    }

    /**
     * Constructs an EventManager with the specified configuration file path.
     *
     * @param configPath the path to the configuration file. Passing an empty string or invalid path will force
     *                   the EventManager to fall back to the default configuration.
     *
     */
    public EventManager(String configPath) {
        this.logHandler = new LogHandler(configPath);
        this.timeFormat = this.logHandler.getConfig().getEvent().getTimeFormat();
        this.eventThread = initiatEventThread();
    }

    /**
     * Creates a new Thread to write events to the log file. It will run indefinitely until it is interrupted (either
     * by calling the {@link EventManager#stopEventThread()} method or by the JVM shutting down).
     * The thread will write events to the log file if the queue is not empty and internal events are enabled.
     * */
    public Thread initiatEventThread() {
        Thread thread = new Thread(() -> {
            try {
                // Run indefinitely until the thread is interrupted or internal events are disabled
                while (!Thread.currentThread().isInterrupted() && logHandler.getConfig().getInternalEvents().isEnabled()) {
                    String event = eventQueue.take();
                    writeEventToLogFile(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
        return thread;
    }

    /**
     * Stops the event thread by interrupting it and waiting for it to finish. This method should be called before
     * shutting down the application to ensure that all events are written to the log file.
     */
    public void stopEventThread() {
        System.out.println("Stopping event thread...");
        eventThread.interrupt();

        // Process remaining events
        while (!eventQueue.isEmpty()) {
            try {
                String event = eventQueue.poll();
                if (event != null) {
                    writeEventToLogFile(event);
                }
            } catch (Exception e) {
                System.err.println("Error writing remaining events: " + e.getMessage());
            }
        }

        try {
            eventThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Event thread stopped successfully.");
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

        writeEventToQueue(event);
    }

    /**
     * Logs a message to the destination file.
     *
     * @param level    the log level of the message.
     * @param messages an object array to be appended to the message.
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

        writeEventToQueue(event);
    }

    /**
     * Writes the event to the log file.
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
            myWriter.write(event + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
    }

    private void writeEventToQueue(String event) {
        this.eventQueue.add(event);
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