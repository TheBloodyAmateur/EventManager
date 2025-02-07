package io.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class ConfigLogRotate {
    private AtomicInteger maxSizeInKB = new AtomicInteger(10240);
    private AtomicInteger rotationPeriodInSeconds = new AtomicInteger(86400);
    private AtomicReference<String> compressionFormat = new AtomicReference<>("gzip");

    public int getMaxSizeInKB() {
        return maxSizeInKB.get();
    }

    public void setMaxSizeInKB(int maxSizeInKB) {
        this.maxSizeInKB.set(maxSizeInKB);
    }

    public int getRotationPeriodInSeconds() {
        return rotationPeriodInSeconds.get();
    }

    public void setRotationPeriodInSeconds(int rotationPeriodInSeconds) {
        this.rotationPeriodInSeconds.set(rotationPeriodInSeconds);
    }

    public String getCompressionFormat() {
        return compressionFormat.get();
    }

    public void setCompressionFormat(String compressionFormat) {
        this.compressionFormat.set(compressionFormat);
    }
}
