package com.github.eventmanager.filehandlers;

import org.junit.jupiter.api.Test;

class LogHandlerTest {

    String configPath = "config/loggingConfig.json";

    @Test
    void checkIfLogFileNeedsRotation() {
        // Create a new LogHandler object
        LogHandler logHandler = new LogHandler(configPath);

        // Check if the log file needs rotation
        logHandler.checkIfLogFileNeedsRotation();
    }
}