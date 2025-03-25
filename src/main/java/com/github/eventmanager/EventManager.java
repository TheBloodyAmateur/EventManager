package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.formatters.EventCreator;
import com.github.eventmanager.formatters.EventFormatter;
import com.github.eventmanager.formatters.KeyValueWrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * The EventManager class is responsible for managing and logging events.
 * It provides methods to log messages with different log levels and formats.
 */
public class EventManager extends ManagerBase {
    private final InternalEventManager internalEventManager;

    /**
     * Constructs an EventManager with the specified LogHandler.
     *
     * @param logHandler the LogHandler to use for logging events.
     */
    public EventManager(LogHandler logHandler) {
        super(logHandler);
        this.internalEventManager = this.logHandler.getInternalEventManager();
        internalEventManager.logInfo("EventManager started successfully.");
        initiateThreads();
    }

    /**
     * Constructs an EventManager with the specified configuration file path.
     *
     * @param configPath the path to the configuration file. Passing an empty string or invalid path will force
     *                   the EventManager to fall back to the default configuration.
     *
     */
    public EventManager(String configPath) {
        super(configPath);
        this.internalEventManager = this.logHandler.getInternalEventManager();
        internalEventManager.logInfo("EventManager started successfully.");
        internalEventManager.logInfo("Initializing event thread...");
        initiateThreads();
    }

    /**
     * Stops the event thread by interrupting it and waiting for it to finish. This method should be called before
     * shutting down the application to ensure that all events are written to the log file.
     */
    public void stopEventThread() {
        internalEventManager.logInfo("Stopping processing thread gracefully...");

        stopProcessingThread(internalEventManager);
        stopEventThread(internalEventManager);

        try {
            processingThread.join();
            eventThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            internalEventManager.logError("Error stopping threads: " + e.getMessage() + ", Thread interrupted forcefully.");
        }
        internalEventManager.logInfo("Event thread stopped successfully.");
        internalEventManager.logInfo("EventManager stopped successfully. Shutting down internal event manager...");
        internalEventManager.stopPipeline();
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
     * @param level    the log level of the message.
     * @param messages an object array to be appended to the message.
     */
    private void logMessage(String level, KeyValueWrapper... messages) {
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();
        Map<String, String> metaData = setMetaDataFields(level);

        String event = switch (eventFormat) {
            case "kv" -> EventFormatter.KEY_VALUE.format(metaData, messages);
            case "csv" -> EventFormatter.CSV.format(metaData, messages);
            case "xml" -> EventFormatter.XML.format(metaData, messages);
            case "json" -> EventFormatter.JSON.format(metaData, messages);
            default -> EventFormatter.DEFAULT.format(metaData, messages);
        };

        writeEventToProcessingQueue(event);
    }

    protected void writeEventToLogFile(String event) {
        if (this.logHandler.getConfig().getEvent().getPrintToConsole()) {
            System.out.println(event);
            return;
        } else if (this.logHandler.getConfig().getEvent().getPrintAndSaveToFile()) {
            System.out.println(event);
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
            internalEventManager.logError("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
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
     * Logs a fatal message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logFatalMessage(EventCreator message) {writeEventToQueue(message.create());}

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
     * Logs an error message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logErrorMessage(EventCreator message) {writeEventToQueue(message.create());}

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
     * Logs a warning message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logWarningMessage(EventCreator message) {writeEventToQueue(message.create());}

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
     * Logs an informational message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logInfoMessage(EventCreator message) {
        if (areInfoLogsEnabled()) {
            writeEventToQueue(message.create());
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

    /**
     * Logs a debug message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logDebugMessage(EventCreator message) {
        if (this.logHandler.getConfig().getEvent().getDebuggingMode()) {
            writeEventToQueue(message.create());
        }
    }
}