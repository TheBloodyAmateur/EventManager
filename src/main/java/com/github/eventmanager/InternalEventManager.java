package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The InternalEventManager class is responsible for managing internal events of the EventManager library. It provides
 * a reliable way to identify and troubleshoot possibly issues within the library.
 * */
public final class InternalEventManager extends ManagerBase {
    private final String prefix = "INTERNAL:";
    public InternalEventManager(LogHandler logHandler) {
        super(logHandler);
        initiateThreads();
    }

    /**
     * Stops the internal event thread by interrupting it and waiting for it to finish. This method should be called
     * before shutting down the application to ensure that all internal events are written to the log file.
     * */
    public void stopPipeline() {
        stopAllThreads();
    }

    /**
     * Logs a message with the specified level and message to the log file.
     * @param event the event to log.
     * */
    protected void writeEventToLogFile(String event) {
        if (this.logHandler.getConfig().getEvent().getPrintToConsole()) {
            System.out.println(event);
            return;
        } else if (this.logHandler.getConfig().getEvent().getPrintAndSaveToFile()) {
            System.out.println(event);
        }

        try {
            if (!this.logHandler.checkIfInternalLogFileExists()) {
                this.logHandler.createLogFile();
            }
            String filePath = this.logHandler.getConfig().getInternalEvents().getFilePath();
            FileWriter myWriter = new FileWriter(filePath + this.logHandler.getCurrentInternalFileName(), true);
            myWriter.write(event + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
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
