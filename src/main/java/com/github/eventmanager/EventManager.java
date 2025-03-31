package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.formatters.EventCreator;
import com.github.eventmanager.formatters.EventFormatter;
import com.github.eventmanager.formatters.KeyValueWrapper;
import com.github.eventmanager.helpers.EventMetaDataBuilder;

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
        initiateThreads(internalEventManager);
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
        initiateThreads(internalEventManager);
    }

    /**
     * Stops the event thread by interrupting it and waiting for it to finish. This method should be called before
     * shutting down the application to ensure that all events are written to the log file.
     *
     * @deprecated Use {@link #stopPipeline()} instead.
     */
    @Deprecated
    public void stopEventThread() {
        stopAllThreads(this.internalEventManager);
        internalEventManager.logInfo("Event thread stopped successfully.");
        internalEventManager.logInfo("EventManager stopped successfully. Shutting down internal event manager...");
        internalEventManager.stopPipeline();
    }

    /**
     * Stops the event thread by interrupting it and waiting for it to finish. This method should be called before
     * shutting down the application to ensure that all events are written to the log file.
     */
    public void stopPipeline() {
        stopAllThreads(this.internalEventManager);
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
    public void logFatalMessage(EventCreator message) {
        writeEventToQueue(message.create());
    }

    /**
     * Logs a fatal message with an exception stack trace.
     *
     * @param exception the exception to log.
     */
    public void logFatalMessage(Exception exception) {
        String stackTrace = castExceptionStackTraceToString(exception);
        logMessage("FATAL",stackTrace);
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
     * Logs an error message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logErrorMessage(EventCreator message) {
        writeEventToQueue(message.create());
    }

    /**
     * Logs an error message with an exception stack trace.
     *
     * @param exception the exception to log.
     */
    public void logErrorMessage(Exception exception) {
        String stackTrace = castExceptionStackTraceToString(exception);
        logMessage("ERROR", stackTrace);
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
     * Logs a warning message with a EventCreator message.
     *
     * @param message the message to log.
     */
    public void logWarningMessage(EventCreator message) {
        writeEventToQueue(message.create());
    }

    /**
     * Logs a warning message with an exception stack trace.
     *
     * @param exception the exception to log.
     */
    public void logWarningMessage(Exception exception) {
        String stackTrace = castExceptionStackTraceToString(exception);
        logMessage("WARNING", stackTrace);
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
     * Logs an informational message with an exception stack trace.
     *
     * @param exception the exception to log.
     */
    public void logInfoMessage(Exception exception) {
        if (areInfoLogsEnabled()) {
            String stackTrace = castExceptionStackTraceToString(exception);
            logMessage("INFO", stackTrace);
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

    /**
     * Logs a debug message with an exception stack trace.
     *
     * @param exception the exception to log.
     */
    public void logDebugMessage(Exception exception) {
        if (this.logHandler.getConfig().getEvent().getDebuggingMode()) {
            String stackTrace = castExceptionStackTraceToString(exception);
            logMessage("DEBUG", stackTrace);
        }
    }
}