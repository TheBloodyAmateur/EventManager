package dev.github.eventmanager.filehandlers.config;

import lombok.Getter;

@Getter
public class Config {
    private ConfigEvent event;
    private ConfigLogFile logFile;
    private ConfigLogRotate logRotateConfig;
}
