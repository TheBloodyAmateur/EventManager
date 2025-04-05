package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.internal.ManagerBase;

/**
 * The InternalEventManager class is responsible for managing internal events of the EventManager library. It provides
 * a reliable way to identify and troubleshoot possibly issues within the library.
 * */
public final class InternalEventManager extends ManagerBase {
    private final String prefix = "INTERNAL:";
    public InternalEventManager(LogHandler logHandler) {
        super(logHandler);
        initiateThreads();
        logInfo("InternalEventManager started successfully.");
    }

    /**
     * Stops the internal event thread by interrupting it and waiting for it to finish. This method should be called
     * before shutting down the application to ensure that all internal events are written to the log file.
     * */
    public void stopPipeline() {
        stopAllThreads();
    }

    /**
     * Log a fatal events to the log file.
     * @param message the message to log.
     * */
    public void logFatal(String message) {
        if (this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "FATAL", message);
        }
    }

    /**
     * Log an error event to the log file.
     * @param message the message to log.
     * */
    public void logError(String message) {
        if (this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "ERROR", message);
        }
    }

    /**
     * Log a warning event to the log file.
     * @param message the message to log.
     * */
    public void logWarn(String message) {
        if (this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "WARN", message);
        }
    }

    /**
     * Log an informational event to the log file.
     * @param message the message to log.
     * */
    public void logInfo(String message) {
        if (areInfoLogsEnabled() && this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "INFO", message);
        }
    }

    /**
     * Log a debug event to the log file.
     * @param message the message to log.
     * */
    public void logDebug(String message) {
        if (areInfoLogsEnabled() && this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "DEBUG", message);
        }
    }

    public boolean areInfoLogsEnabled() {
        boolean informationalMode = this.logHandler.getConfig().getEvent().getInformationalMode();
        boolean debuggingMode = this.logHandler.getConfig().getEvent().getDebuggingMode();
        return informationalMode || debuggingMode;
    }
}
