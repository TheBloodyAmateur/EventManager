package com.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class contains the configuration settings for the internal events file handler.
 * <p>
 * The configuration includes settings for the file path, file name, file extension, and whether the file handler is enabled.
 * </p>
 */
public class ConfigInternalEvents {
    private final AtomicReference<String> filePath = new AtomicReference<>("/tmp/");
    private final AtomicReference<String> fileName = new AtomicReference<>("internal");
    private final AtomicReference<String> fileExtension = new AtomicReference<>(".log");
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    /**
     * Gets the file path where the internal events log will be stored.
     *
     * @return the file path as a {@link String}
     */
    public String getFilePath() {
        return this.filePath.get();
    }

    /**
     * Sets the file path where the internal events log will be stored.
     *
     * @param filePath the file path as a {@link String}
     */
    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    /**
     * Gets the file name for the internal events log.
     *
     * @return the file name as a {@link String}
     */
    public String getFileName() {
        return this.fileName.get();
    }

    /**
     * Sets the file name for the internal events log.
     *
     * @param fileName the file name as a {@link String}
     */
    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    /**
     * Gets the file extension for the internal events log.
     *
     * @return the file extension as a {@link String}
     */
    public String getFileExtension() {
        return this.fileExtension.get();
    }

    /**
     * Sets the file extension for the internal events log.
     *
     * @param fileExtension the file extension as a {@link String}
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension.set(fileExtension);
    }

    /**
     * Checks if the internal events file handler is enabled.
     *
     * @return {@code true} if the file handler is enabled, {@code false} otherwise
     */
    public boolean isEnabled() {
        return this.enabled.get();
    }

    /**
     * Enables or disables the internal events file handler.
     *
     * @param enabled {@code true} to enable the file handler, {@code false} to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }
}