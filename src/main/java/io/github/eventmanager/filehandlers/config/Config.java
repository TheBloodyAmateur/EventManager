package io.github.eventmanager.filehandlers.config;

import lombok.Getter;

@Getter
public class Config {
    private ConfigEvent event = new ConfigEvent();
    private ConfigLogFile logFile = new ConfigLogFile();
    private ConfigLogRotate logRotateConfig = new ConfigLogRotate();
}
