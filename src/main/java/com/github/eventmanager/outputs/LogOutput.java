package com.github.eventmanager.outputs;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.internal.InternalEventLogger;

import java.io.FileWriter;
import java.io.IOException;

public class LogOutput implements Output {
    @Override
    public void write(LogHandler loghandler, String event) {
        try {
            if (!loghandler.checkIfInternalLogFileExists()) {
                loghandler.createInternalLogFile();
            }
            String filePath = loghandler.getConfig().getInternalEvents().getFilePath();
            FileWriter myWriter = new FileWriter(filePath + loghandler.getCurrentInternalFileName(), true);
            myWriter.write(event + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
    }

    @Override
    public void write(InternalEventLogger internalEventLogger, String event) {
        try {
            if (!internalEventLogger.getLogHandler().checkIfLogFileExists()) {
                internalEventLogger.getLogHandler().createLogFile();
            }
            String filePath = internalEventLogger.getLogHandler().getConfig().getLogFile().getFilePath();
            FileWriter myWriter = new FileWriter(filePath + internalEventLogger.getLogHandler().getCurrentFileName(), true);
            myWriter.write(event + "\n");
            myWriter.close();
        } catch (IOException e) {
            internalEventLogger.logError("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
    }
}
