package dev.github.eventmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.github.eventmanager.filehandlers.ConfigLoader;
import dev.github.eventmanager.filehandlers.LogHandler;
import dev.github.eventmanager.filehandlers.config.Config;
import dev.github.eventmanager.formatters.KeyValueWrapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EventManagerTest {
    String configPath = "confg/loggingConfig.jasonStatham";

    @Test
    void createInstance() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        EventManager eventManager = new EventManager(logHandler);
    }

    @Test
    void createDefaultEvents() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage( "This is an informational message");
        eventManager.logWarningMessage( "This is an error message");
        eventManager.logFatalMessage( "This is a fatal message");

        // Load the config file
        if (printToConsoleIsEnabled()) return;

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
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        // Load the config file
        if (printToConsoleIsEnabled()) return;

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
    void createKVEvents() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("kv");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("key=\"value\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("gisela=\"brünhilde\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("darth=\"vader\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createKVEventsWithArguments() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("key=\"value\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("gisela=\"brünhilde\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("darth=\"vader\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createCSVEvents() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("csv");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("test1");
        eventManager.logWarningMessage("test2");
        eventManager.logFatalMessage("test3");

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("test1")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("test2")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("test3")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createCSVEventsWithArguments() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("csv");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("value,key")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("brünhilde,herzig")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("vader,skywalker")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createXMLEvents() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("xml");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("test1");
        eventManager.logWarningMessage("test2");
        eventManager.logFatalMessage("test3");

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("<message>test1</message>")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("<message>test2</message>")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("<message>test3</message>")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createXMLEventsWithArguments() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("xml");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("<key>value</key><value>key</value>")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("<gisela>brünhilde</gisela><detlef>herzig</detlef>")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("<darth>vader</darth><luke>skywalker</luke>")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createJSONEvents() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("json");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("test1");
        eventManager.logWarningMessage("test2");
        eventManager.logFatalMessage("test3");

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"message\":\"test1\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"message\":\"test2\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"message\":\"test3\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createJSONEventsWithArguments() {
        LogHandler logHandler = new LogHandler(new ConfigLoader(configPath));
        logHandler.getConfig().getEvent().setEventFormat("json");
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        if (printToConsoleIsEnabled()) return;

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"key\":\"value\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"value\":\"key\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"gisela\":\"brünhilde\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"detlef\":\"herzig\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"darth\":\"vader\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"luke\":\"skywalker\"")));
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    private boolean printToConsoleIsEnabled() {
        // Load the config file
        try{
            String path = EventManager.setCorrectOSSeperator(configPath);
            path = System.getProperty("user.dir")+ File.separator+path;
            path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(new File(path), Config.class);
            if(config.getEvent().getPrintToConsole()){
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}