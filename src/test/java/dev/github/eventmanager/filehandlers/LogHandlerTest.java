package dev.github.eventmanager.filehandlers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class LogHandlerTest {

    @Test
    void checkIfLogFileNeedsRotation() {
        ConfigHandler configHandler = new ConfigHandler();
        LogHandler logHandler = new LogHandler(configHandler);
        logHandler.checkIfLogFileNeedsRotation();
    }

    @Test
    void rotateLgFile() {

    }

    @Test
    void checkIfLogFileExists() {
    }
}