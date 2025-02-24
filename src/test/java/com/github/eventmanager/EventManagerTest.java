package com.github.eventmanager;

import com.github.eventmanager.filehandlers.ConfigLoader;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.formatters.KeyValueWrapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EventManagerTest {
    String configPath = "config/loggingConfig.json";

    @Test
    void createInstance() {
        LogHandler logHandler = new LogHandler(configPath);
        EventManager eventManager = new EventManager(logHandler);
    }

    @Test
    void createDefaultEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("default");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage( "This is an informational message");
        eventManager.logWarningMessage( "This is an error message");
        eventManager.logFatalMessage( "This is a fatal message");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is an informational message")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is an error message")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is a fatal message")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createDefaultEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("default");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is a fatal message")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createKVEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("kv");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("key=\"value\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createKVEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("key=\"value\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createCSVEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("csv");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage("test3");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("test3")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createCSVEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("csv");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("vader,skywalker")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createXMLEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("xml");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage("test3");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("<message>test3</message>")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createXMLEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("xml");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("<darth>vader</darth><luke>skywalker</luke>")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createJSONEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("json");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("test1");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"message\":\"test1\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createJSONEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("json");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"key\":\"value\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }
}