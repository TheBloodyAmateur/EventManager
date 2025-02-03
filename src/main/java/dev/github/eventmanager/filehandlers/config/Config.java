package dev.github.eventmanager.filehandlers.config;

import lombok.Getter;

@Getter
public class Config {
    private boolean printToConsole;
    private boolean printAndSaveToFile;
    private boolean debuggingMode;
    private boolean informationalMode;
    private String timeFormat;
    private ConfigLogFile logFile;
    private ConfigLogRotate logRotateConfig;
}
