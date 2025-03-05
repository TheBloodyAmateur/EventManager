package com.github.eventmanager.filehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eventmanager.EventManager;
import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.compressors.Gzip;
import com.github.eventmanager.compressors.Zip;
import com.github.eventmanager.filehandlers.config.Config;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The LogHandler class is responsible for managing log files for the EventManager application.
 * It handles log file creation, rotation based on size and time, and reading log file content.
 * @since 1.0
 */
@Getter
public class LogHandler {
    private Config config;
    @Setter
    private String currentFileName;
    @Getter
    @Setter
    private String currentInternalFileName;
    private InternalEventManager internalEventManager;

    /**
     * Constructs a LogHandler with the specified ConfigLoader.
     *
     * @param configPath the path to the configuration file.
     */
    public LogHandler(String configPath) {
        this.loadConfigFile(configPath);
        String fileName = this.config.getLogFile().getFileName();
        String fileExtension = this.config.getLogFile().getFileExtension();
        this.currentFileName = this.createNewFileName(fileName, fileExtension);
    }

    /**
     * Loads the configuration file from the specified path.
     * If the file cannot be loaded, default configuration values are used.
     *
     * @param configPath the path to the configuration file.
     */
    private void loadConfigFile(String configPath) {
        // Get the path of the file and decode it to UTF-8 to cope with special characters
        configPath = EventManager.setCorrectOSSeperator(configPath);
        String path = System.getProperty("user.dir") + File.separator + configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Load the config file
        try {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(new File(path), Config.class);
            initialiseInternalEventManager();
            internalEventManager.logInfo("Config file loaded successfully.");
        } catch (Exception e) {
            config = new Config();
            initialiseInternalEventManager();
            internalEventManager.logError("Could not load the config file. Using default values.");
        }
    }



    /**
     * Initialises the internal event manager.
     * */
    private void initialiseInternalEventManager() {
        this.currentInternalFileName = this.createNewFileName(this.config.getInternalEvents().getFileName(), this.config.getInternalEvents().getFileExtension());
        createInternalLogFile();
        internalEventManager = new InternalEventManager(this);
    }

    /**
     * Creates a new file name based on the current time with the format dd-MM-yyyy-HH-mm-ss.
     *
     * @param fileName the base file name.
     * @param fileExtension the file extension.
     * @return the new file name.
     */
    private String createNewFileName(String fileName, String fileExtension) {
        String creationTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
        return fileName + "-" + creationTime + fileExtension;
    }

    /**
     * Checks if the log file needs rotation based on size and time.
     */
    public void checkIfLogFileNeedsRotation() {
        File directory = new File(this.config.getLogFile().getFilePath());
        File[] files = directory.listFiles();

        String fileName = this.config.getLogFile().getFileName();
        Pattern pattern = Pattern.compile(fileName + "-(?<fileTimeStamp>[0-9\\-]+).log$");

        try {
            for (File file : files) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    FileTime fileTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                    long size = new File(this.config.getLogFile().getFilePath()).length();
                    long creationTime = fileTime.toMillis() / 1000L;
                    long currentTime = System.currentTimeMillis() / 1000L;

                    if ((currentTime - creationTime) > this.config.getLogRotateConfig().getRotationPeriodInSeconds()) {
                        this.rotateLogFile(file);
                    } else if (size > this.config.getLogRotateConfig().getMaxSizeInKB()) {
                        this.rotateLogFile(file);
                    }
                }
            }
        } catch (Exception e) {
            internalEventManager.logError("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Rotates the log file using the specified compression format.
     *
     * @param file the log file to rotate.
     */
    public void rotateLogFile(File file) {
        switch (this.config.getLogRotateConfig().getCompressionFormat()) {
            case "gzip":
                Gzip.compress(file.getAbsolutePath());
                break;
            case "zip":
                Zip.compress(file.getAbsolutePath());
                break;
        }
        file.delete();
    }

    /**
     * Checks if the log file exists.
     *
     * @return true if the log file exists, false otherwise.
     */
    public boolean checkIfLogFileExists() {
        return new File(this.config.getLogFile().getFilePath()).exists();
    }

    /**
     * Checks if the log file for internal events exists.
     *
     * @return true if the log file exists, false otherwise.
     */
    public boolean checkIfInternalLogFileExists() {
        return new File(this.config.getInternalEvents().getFilePath()).exists();
    }

    /**
     * Creates a new log file.
     */
    public void createLogFile() {
        try {
            File newFile = new File(this.getCurrentFileName());
            newFile.createNewFile();
            this.setCurrentFileName(String.valueOf(newFile));
        } catch (IOException e) {
            internalEventManager.logError("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Creates a new log file for internal events.
     */
    public void createInternalLogFile() {
        try {
            File newFile = new File(this.getCurrentInternalFileName());
            newFile.createNewFile();
            this.setCurrentInternalFileName(String.valueOf(newFile));
        } catch (IOException e) {
            internalEventManager.logError("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Reads the log file and returns its content as a list of strings.
     *
     * @return the content of the log file as a list of strings.
     */
    public List<String> readLogFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(this.config.getLogFile().getFilePath()));
            return reader.lines().toList();
        } catch (IOException e) {
            internalEventManager.logError("An error occurred: " + e.getMessage());
        }
        return null;
    }
}