package dev.github.eventmanager.filehandlers.config;

import lombok.Getter;

@Getter
public class ConfigLogFile {
    private String filePath = "/tmp/";
    private String fileName = "application";
    private String fileExtension = ".log";

}
