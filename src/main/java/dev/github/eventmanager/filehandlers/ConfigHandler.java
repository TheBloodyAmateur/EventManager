package dev.github.eventmanager.filehandlers;

import dev.github.eventmanager.EventManager;
import dev.github.eventmanager.formatters.Formatter;
import dev.github.eventmanager.formatters.TimeStampFormatter;
import lombok.Getter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
    @Getter
    boolean debugModeOn = false;

    @Getter
    boolean informationalModeOn = false;

    @Getter
    String filePath = "/var/log/";

    @Getter
    String fileName = "application";

    @Getter
    String fileExtension = "log";

    @Getter
    int maxSizeInKB = 1024;

    @Getter
    int rotationPeriodInSeconds = 86400;

    List<String> availableTimeFormats;

    @Getter
    String timeFormat;

    @Getter
    String compressionFormat;

    public ConfigHandler() {
        readConfigFile();
    }

    private void readConfigFile() {
        JSONObject config;
        List<String> formats = new ArrayList<String>();
        List<String> replacement = Arrays.asList("dd-MMM-yyyy HH:mm:ss z", "dd.MM.yyyy h:mm:ss.SSS a z", "E, MMM dd yyyy HH:mm:ss z");

        // Get the path of the file and decode it to UTF-8 to cope with special characters
        String configPath = EventManager.setCorrectOSSeperator("config/loggingConfig.json");
        String path = System.getProperty("user.dir")+ File.separator+configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Setting a default time format in case of an error
        this.timeFormat = replacement.getFirst();


        // Error handling of the file processing part
        try {
            FileReader fileReader = new FileReader(path);
            config = new JSONObject(new JSONTokener(fileReader));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.availableTimeFormats = replacement;
            return;
        }

        try {
            debugModeOn = config.getBoolean("debuggingMode");
            informationalModeOn = config.getBoolean("informationalMode");
            timeFormat = config.getString("timeFormat");
            JSONObject file = config.getJSONObject("file");
            filePath = file.getString("filePath");
            fileName = file.getString("fileName");
            fileExtension = file.getString("fileExtension");
            JSONObject logRotate = config.getJSONObject("logRotate");
            maxSizeInKB = logRotate.getInt("maxSizeInKB");
            rotationPeriodInSeconds = logRotate.getInt("rotationPeriodInSeconds");
            compressionFormat = logRotate.getString("compressionFormat");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.availableTimeFormats = formats;
    }
}
