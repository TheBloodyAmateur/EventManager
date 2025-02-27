package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;

import java.io.FileWriter;
import java.io.IOException;

public final class InternalEventManager extends ManagerBase {
    private final String prefix = "INTERNAL:";
    public InternalEventManager(LogHandler logHandler) {
        super(logHandler);
        this.eventThread = initiatEventThread();
    }

    @Override
    public void stopEventThread() {
        System.out.println("Stopping event thread...");
        eventThread.interrupt();

        // Process remaining events
        while (!eventQueue.isEmpty() && logHandler.getConfig().getInternalEvents().isEnabled()) {
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

    protected void writeEventToLogFile(String event) {
        if (this.logHandler.getConfig().getEvent().getPrintToConsole()) {
            System.out.print(event);
            return;
        } else if (this.logHandler.getConfig().getEvent().getPrintAndSaveToFile()) {
            System.out.print(event);
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

    public void logFatal(String message) {
        if (this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "FATAL", message);
        }
    }

    public void logError(String message) {
        if (this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "ERROR", message);
        }
    }

    public void logWarn(String message) {
        if (this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "WARN", message);
        }
    }

    public void logInfo(String message) {
        if (areInfoLogsEnabled() && this.logHandler.getConfig().getInternalEvents().isEnabled()) {
            logMessage(prefix + "INFO", message);
        }
    }

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
