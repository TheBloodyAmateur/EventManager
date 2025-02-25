package com.github.eventmanager.filehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.config.Config;
import lombok.Getter;

import java.io.File;

/**
 * The ConfigLoader class is responsible for loading the configuration settings for the EventManager application.
 * It reads the configuration from a specified file path and deserializes it into a Config object.
 * @deprecated This class is deprecated and will be removed in a future release.
 * Use the {@link LogHandler} class from the com.github.eventmanager package instead.
 */
@Getter
@Deprecated
public class ConfigLoader {
    private Config config;

    /**
     * Constructs a ConfigLoader with the specified configuration file path.
     *
     * @param configPath the path to the configuration file.
     */
    @Deprecated
    public ConfigLoader(String configPath) {
        loadConfigFile(configPath);
    }

    /**
     * Loads the configuration file from the specified path.
     * If the file cannot be loaded, default configuration values are used.
     *
     * @param configPath the path to the configuration file.
     * @deprecated This method is deprecated and will be removed in a future release.
     */
    @Deprecated
    private void loadConfigFile(String configPath) {
        // Get the path of the file and decode it to UTF-8 to cope with special characters
        configPath = EventManager.setCorrectOSSeperator(configPath);
        String path = System.getProperty("user.dir") + File.separator + configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Load the config file
        try {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(new File(path), Config.class);
        } catch (Exception e) {
            System.out.println("ERROR: Could not load the config file. Using default values.");
            config = new Config();
        }
    }
}