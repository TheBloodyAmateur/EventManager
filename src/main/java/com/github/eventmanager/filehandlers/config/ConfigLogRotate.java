package com.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The ConfigLogRotate class holds the configuration settings related to log file rotation for the EventManager application.
 * It includes settings for the maximum size of log files, the rotation period, and the compression format to use when rotating log files.
 */
public class ConfigLogRotate {
    private AtomicInteger maxSizeInKB = new AtomicInteger(10240);
    private AtomicInteger rotationPeriodInSeconds = new AtomicInteger(86400);
    private AtomicReference<String> compressionFormat = new AtomicReference<>("gzip");

    /**
     * Gets the maximum size of log files in kilobytes before rotation is triggered.
     *
     * @return the maximum size of log files in kilobytes.
     */
    public int getMaxSizeInKB() {
        return maxSizeInKB.get();
    }

    /**
     * Sets the maximum size of log files in kilobytes before rotation is triggered.
     *
     * @param maxSizeInKB the maximum size of log files in kilobytes.
     */
    public void setMaxSizeInKB(int maxSizeInKB) {
        this.maxSizeInKB.set(maxSizeInKB);
    }

    /**
     * Gets the rotation period in seconds after which log files should be rotated.
     *
     * @return the rotation period in seconds.
     */
    public int getRotationPeriodInSeconds() {
        return rotationPeriodInSeconds.get();
    }

    /**
     * Sets the rotation period in seconds after which log files should be rotated.
     *
     * @param rotationPeriodInSeconds the rotation period in seconds.
     */
    public void setRotationPeriodInSeconds(int rotationPeriodInSeconds) {
        this.rotationPeriodInSeconds.set(rotationPeriodInSeconds);
    }

    /**
     * Gets the compression format to use when rotating log files.
     *
     * @return the compression format.
     */
    public String getCompressionFormat() {
        return compressionFormat.get();
    }

    /**
     * Sets the compression format to use when rotating log files.
     *
     * @param compressionFormat the compression format.
     */
    public void setCompressionFormat(String compressionFormat) {
        this.compressionFormat.set(compressionFormat);
    }
}