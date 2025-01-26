package dev.github.eventmanager.filehandlers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ConfigHandlerTest {
    @Test
    void createInstance() {
        ConfigHandler configHandler = new ConfigHandler();
        assertNotNull(configHandler);
    }

    @Test
    void readConfig() {
        ConfigHandler configHandler = new ConfigHandler();
        assertNotNull(configHandler.printConfig());
    }
}