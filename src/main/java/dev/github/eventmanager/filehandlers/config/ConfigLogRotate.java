package dev.github.eventmanager.filehandlers.config;

import lombok.Getter;

@Getter
public class ConfigLogRotate {
    private int maxSizeInKB = 1024;
    private int rotationPeriodInSeconds = 86400;
    private String compressionFormat = "gzip";

}
