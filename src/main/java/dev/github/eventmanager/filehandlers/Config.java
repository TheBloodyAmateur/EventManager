package dev.github.eventmanager.filehandlers;

import lombok.Getter;

@Getter
public class Config {
    private boolean debuggingMode;
    private boolean informationalMode;
    private String timeFormat;
    private ConfigLogFile logFile;
    private ConfigLogRotate logRotateConfig;
}
