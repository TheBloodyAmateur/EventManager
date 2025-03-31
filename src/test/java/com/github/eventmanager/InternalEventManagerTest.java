package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InternalEventManagerTest {
    private String configPath = "config/.json";
    private EventManager eventManager;

    // Wait for events to be logged because the event thread is asynchronous
    void waitForEvents() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        eventManager.stopPipeline();
    }

    @Test
    void checkIfInternalLogsWereCreated() {
        LogHandler logHandler = new LogHandler(configPath);
        this.eventManager = new EventManager(logHandler);

        eventManager.logWarningMessage("This is a warning message.");
        eventManager.logErrorMessage("This is an error message.");
        eventManager.logInfoMessage("This is an info message.");

        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getInternalEvents().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentInternalFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("INTERNAL:ERROR")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e);
        }
    }
}