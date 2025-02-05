package dev.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ConfigEvent {
    private boolean printToConsole = false;
    private boolean printAndSaveToFile = false;
    private boolean debuggingMode = false;
    private boolean informationalMode = false;
    private String timeFormat = "dd.MM.yyyy h:mm:ss.SSS a z";
    @Setter
    private String eventFormat = "default";
}
