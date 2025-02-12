package com.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The ConfigLogFile class holds the configuration settings related to log files for the EventManager application.
 * It includes settings for the file path, file name, and file extension of the log files.
 */
public class ConfigLogFile {
    private AtomicReference<String> filePath = new AtomicReference<>("/tmp/");
    private AtomicReference<String> fileName = new AtomicReference<>("application");
    private AtomicReference<String> fileExtension = new AtomicReference<>(".log");

    /**
     * Gets the file path for the log files.
     *
     * @return the file path for the log files.
     */
    public String getFilePath() {
        return filePath.get();
    }

    /**
     * Sets the file path for the log files.
     *
     * @param filePath the file path for the log files.
     */
    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    /**
     * Gets the file name for the log files.
     *
     * @return the file name for the log files.
     */
    public String getFileName() {
        return fileName.get();
    }

    /**
     * Sets the file name for the log files.
     *
     * @param fileName the file name for the log files.
     */
    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    /**
     * Gets the file extension for the log files.
     *
     * @return the file extension for the log files.
     */
    public String getFileExtension() {
        return fileExtension.get();
    }

    /**
     * Sets the file extension for the log files.
     *
     * @param fileExtension the file extension for the log files.
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension.set(fileExtension);
    }
}