package com.github.thebloodyamateur.eventmanager.filehandlers;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LogHandler {
    @Getter
    @Setter
    public String fileName;
    @Getter
    boolean debugModeOn = false;
    @Getter
    boolean informationalModeOn = false;
    @Getter
    private String timeFormat;
    @Getter
    public String filePath;
    private int rotationSize;
    private int maxSizeInKB;
    private int rotationPeriodInSeconds;
    private long lastRotationTime;

    public LogHandler(ConfigHandler configHandler) {
        this.filePath = configHandler.filePath;
        this.fileName = this.createNewFileName(configHandler.fileName, configHandler.fileExtension);
        this.rotationSize = configHandler.maxSizeInKB;
        this.maxSizeInKB = configHandler.maxSizeInKB;
        this.rotationPeriodInSeconds = configHandler.rotationPeriodInSeconds;
        this.debugModeOn = configHandler.debugModeOn;
        this.informationalModeOn = configHandler.informationalModeOn;
        this.timeFormat = configHandler.timeFormat;
    }

    private String createNewFileName(String fileName, String fileExtension) {
        long lastRotationTime = System.currentTimeMillis();
        return fileName + "-" + lastRotationTime + fileExtension;
    }

    public void checkIfLogFileNeedsRotation(){
        // TODO Check if the log file needs rotation
    }

    public void rotateLgFile(){
        // TODO Rotate the log file
    }

    public boolean checkIfLogFileExists(){
        return new File(this.filePath).exists();
    }

    public void createLogFile(){
        try {
            this.setFileName(String.valueOf(new File(this.getFileName()).createNewFile()));
        } catch (IOException e) {
            System.out.println("An error occurred:"+e.getMessage());
        }
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
