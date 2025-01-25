package com.github.thebloodyamateur.eventmanager.filehandlers;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LogHandler {
    private String filePath = "/var/log/";
    @Getter
    private String fileName;
    @Getter
    boolean debugModeOn = false;
    @Getter
    boolean informationalModeOn = false;
    private int rotationSize;
    private int maxSizeInKB;
    private int rotationPeriodInSeconds;

    public LogHandler(ConfigHandler configHandler) {
        this.filePath = configHandler.filePath;
        this.fileName = this.createNewFileName(configHandler.fileName, configHandler.fileExtension);
        this.rotationSize = configHandler.maxSizeInKB;
        this.maxSizeInKB = configHandler.maxSizeInKB;
        this.rotationPeriodInSeconds = configHandler.rotationPeriodInSeconds;
        this.debugModeOn = configHandler.debugModeOn;
        this.informationalModeOn = configHandler.informationalModeOn;
    }

    private String createNewFileName(String fileName, String fileExtension) {
        long currentDate = System.currentTimeMillis();
        return fileName + "-" + currentDate + fileExtension;
    }

    public void rotateLgFile(){
        // TODO Rotate the log file
    }

    /**
     * Read the log file and return its content as a list of strings.
     *
     * @return The content of the log file as a list of strings.
     * @thows IOException On input error.
     */
    public List<String> readLogFile(){
        BufferedReader reader;
        try {
            // Read the file and return its content as a list of strings
            reader = new BufferedReader(new FileReader(this.filePath));
            return reader.lines().toList();
        } catch (IOException e) {
            System.out.println("An error occurred:"+e.getMessage());
        }
        return null;
    }
}
