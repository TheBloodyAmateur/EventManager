package com.github.eventmanager.outputs;

import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.filehandlers.LogHandler;

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
    public void write(InternalEventManager internalEventManager, String event) {
        try {
            if (!internalEventManager.getLogHandler().checkIfLogFileExists()) {
                internalEventManager.getLogHandler().createLogFile();
            }
            String filePath = internalEventManager.getLogHandler().getConfig().getLogFile().getFilePath();
            FileWriter myWriter = new FileWriter(filePath + internalEventManager.getLogHandler().getCurrentFileName(), true);
            myWriter.write(event + "\n");
            myWriter.close();
        } catch (IOException e) {
            internalEventManager.logError("An error occurred in writeEventToLogFile:" + e.getMessage());
        }
    }
}
