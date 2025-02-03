package dev.github.eventmanager.filehandlers;

import org.junit.jupiter.api.Test;

class LogHandlerTest {

    @Test
    void checkIfLogFileNeedsRotation() {
        ConfigLoader configLoader = new ConfigLoader();
        LogHandler logHandler = new LogHandler(configLoader);
        logHandler.checkIfLogFileNeedsRotation();
    }

    @Test
    void rotateLgFile() {

    }

    @Test
    void checkIfLogFileExists() {
    }
}