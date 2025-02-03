package dev.github.eventmanager.filehandlers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ConfigLoaderTest {
    @Test
    void createInstance() {
        ConfigLoader configLoader = new ConfigLoader();
        assertNotNull(configLoader);
    }
}