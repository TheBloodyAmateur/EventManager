package com.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigLogFile {
    private AtomicReference<String> filePath = new AtomicReference<>("/tmp/");
    private AtomicReference<String> fileName = new AtomicReference<>("application");
    private AtomicReference<String> fileExtension = new AtomicReference<>(".log");

    public String getFilePath() {
        return filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getFileExtension() {
        return fileExtension.get();
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension.set(fileExtension);
    }
}
