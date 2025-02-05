package dev.github.eventmanager.filehandlers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class LogHandlerTest {

    String configPath = "config/loggingConfig.json";

    @Test
    void checkIfLogFileNeedsRotation() {
        // Create a new LogHandler object
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));

        // Check if the log file needs rotation
        logHandler.checkIfLogFileNeedsRotation();
    }
}