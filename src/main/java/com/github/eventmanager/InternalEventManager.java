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

    @Override
    public void stopEventThread() {
        System.out.println("Stopping processing thread gracefully...");
        processingThread.interrupt();

        // Process remaining events in the processing queue
        while (!processingQueue.isEmpty()) {
            try {
                String event = processingQueue.poll();
                if (event != null) {
                    event = processEvent(event);
                    writeEventToQueue(event);
                }
            } catch (Exception e) {
                System.out.println("Error writing remaining events: " + e.getMessage());
            }
        }
        System.out.println("processingQueue was successfully processed.");

        System.out.println("Stopping processing thread gracefully...");
        eventThread.interrupt();

        // Process remaining events
        while (!eventQueue.isEmpty()) {
            try {
                String event = eventQueue.poll();
                if (event != null) {
                    writeEventToLogFile(event);
                }
            } catch (Exception e) {
                System.out.println("Error writing remaining events: " + e.getMessage());
            }
        }
        System.out.println("eventQueue was successfully processed.");

        try {
            processingThread.join();
            eventThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error stopping threads: " + e.getMessage() + ", Thread interrupted forcefully.");
        }
        System.out.println("Event thread stopped successfully.");
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
