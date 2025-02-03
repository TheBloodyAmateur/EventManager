package dev.github.eventmanager.filehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.github.eventmanager.EventManager;
import lombok.Getter;

import java.io.File;

@Getter
public class ConfigLoader {
    private Config config;
    public ConfigLoader() {
        loadConfigFile();
    }

    private void loadConfigFile() {
        // Get the path of the file and decode it to UTF-8 to cope with special characters
        String configPath = EventManager.setCorrectOSSeperator("config/loggingConfig.json");
        String path = System.getProperty("user.dir")+ File.separator+configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Load the config file
        try{
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(new File(path), Config.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
