package dev.github.eventmanager.filehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.github.eventmanager.EventManager;
import dev.github.eventmanager.filehandlers.config.Config;
import lombok.Getter;

import java.io.File;

@Getter
public class ConfigLoader {
    private Config config;
    public ConfigLoader(String configPath) {
        loadConfigFile(configPath);
    }

    private void loadConfigFile(String configPath) {
        // Get the path of the file and decode it to UTF-8 to cope with special characters
        configPath = EventManager.setCorrectOSSeperator(configPath);
        String path = System.getProperty("user.dir")+ File.separator+configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Load the config file
        try{
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(new File(path), Config.class);
        } catch (Exception e) {
            System.out.println("ERROR: Could not load the config file. Using default values.");
            config = new Config();
        }
    }
}
