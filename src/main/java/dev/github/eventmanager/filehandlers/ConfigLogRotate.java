package dev.github.eventmanager.filehandlers;

import lombok.Getter;

@Getter
class ConfigLogRotate {
    private int maxSizeInKB;
    private int rotationPeriodInSeconds;
    private String compressionFormat;

}
