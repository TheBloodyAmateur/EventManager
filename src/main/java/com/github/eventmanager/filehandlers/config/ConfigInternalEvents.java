package com.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigInternalEvents {
    private final AtomicReference<String> filePath = new AtomicReference<>("/tmp/");
    private final AtomicReference<String> fileName = new AtomicReference<>("internal");
    private final AtomicReference<String> fileExtension = new AtomicReference<>(".log");

    public String getFilePath() {
        return this.filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public String getFileName() {
        return this.fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getFileExtension() {
        return this.fileExtension.get();
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension.set(fileExtension);
    }
}
