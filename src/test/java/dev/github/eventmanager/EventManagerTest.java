package dev.github.eventmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.github.eventmanager.filehandlers.ConfigLoader;
import dev.github.eventmanager.filehandlers.LogHandler;
import dev.github.eventmanager.filehandlers.config.Config;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EventManagerTest {
    @Test
    void createInstance() {
        LogHandler logHandler = new LogHandler(new ConfigLoader());
        EventManager eventManager = new EventManager(logHandler);
    }

    @Test
    void createDefaultEvents() {
        LogHandler logHandler = new LogHandler(new ConfigLoader());
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage( "This is an informational message");
        eventManager.logWarningMessage( "This is an error message");
        eventManager.logFatalMessage( "This is a fatal message");

        String configPath = EventManager.setCorrectOSSeperator("config/loggingConfig.json");
        String path = System.getProperty("user.dir")+ File.separator+configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Load the config file
        try{
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(new File(path), Config.class);
            if(config.getEvent().isPrintToConsole()){
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
}