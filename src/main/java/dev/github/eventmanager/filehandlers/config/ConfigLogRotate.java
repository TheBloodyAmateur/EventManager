package dev.github.eventmanager.filehandlers.config;

import lombok.Getter;

@Getter
public class ConfigLogRotate {
    private int maxSizeInKB;
    private int rotationPeriodInSeconds;
    private String compressionFormat;

}
