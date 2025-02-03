package dev.github.eventmanager.filehandlers;

import dev.github.eventmanager.compressors.Gzip;
import dev.github.eventmanager.compressors.Zip;
import dev.github.eventmanager.filehandlers.config.Config;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogHandler {
    @Getter
    private Config config;
    @Getter
    @Setter
    public String currentFileName;

    public LogHandler(ConfigLoader configLoader) {
        this.config = configLoader.getConfig();
        String fileName = this.config.getLogFile().getFileName();
        String fileExtension = this.config.getLogFile().getFileExtension();
        this.currentFileName = this.createNewFileName(fileName, fileExtension);
    }

    private String createNewFileName(String fileName, String fileExtension) {
        // Create a new file name based on the current time with the format ddMMyyyy
        String creationTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
        return fileName + "-" + creationTime + fileExtension;
    }

    public void checkIfLogFileNeedsRotation() {
        // TODO Check if the log file needs rotation

        // Get all log files in the current directory
        File directory = new File(this.config.getLogFile().getFilePath());
        File[] files = directory.listFiles();

        // Create a pattern to match the log file names and extract the timestamp
        String fileName = this.config.getLogFile().getFileName();
        Pattern pattern = Pattern.compile(fileName + "-(?<fileTimeStamp>[0-9\\-]+).log");

        try {
            for (File file : files) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    // Get the creation time of the log file and the current time
                    FileTime fileTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                    long size = new File(this.config.getLogFile().getFilePath()).length();
                    long creationTime = fileTime.toMillis() / 1000L;
                    long currentTime = System.currentTimeMillis() / 1000L;

                    //System.out.println("File: " + file.getName() + " " + (currentTime - creationTime));

                    // Check if the log file needs rotation based on the rotation period
                    if ((currentTime - creationTime) > this.config.getLogRotateConfig().getRotationPeriodInSeconds()) {
                        this.rotateLogFile(file);
                    }
                    // Check if the log file size needs rotation based on the rotation size
                    else if (size > this.config.getLogRotateConfig().getMaxSizeInKB()) {
                        this.rotateLogFile(file);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void rotateLogFile(File file) {
        if(this.config.getLogRotateConfig().getCompressionFormat().equals("gzip")) {
            // Compress the log file using Gzip
            Gzip.compress(file.getAbsolutePath());
        } else if(this.config.getLogRotateConfig().getCompressionFormat().equals("zip")) {
            // Compress the log file using Zip
            Zip.compress(file.getAbsolutePath());
        }
    }

    public boolean checkIfLogFileExists() {
        return new File(this.config.getLogFile().getFilePath()).exists();
    }

    public void createLogFile() {
        try {
            this.setCurrentFileName(String.valueOf(new File(this.getCurrentFileName()).createNewFile()));
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Read the log file and return its content as a list of strings.
     *
     * @return The content of the log file as a list of strings.
     * @throws IOException On input error.
     */
    public List<String> readLogFile() {
        BufferedReader reader;
        try {
            // Read the file and return its content as a list of strings
            reader = new BufferedReader(new FileReader(this.config.getLogFile().getFilePath()));
            return reader.lines().toList();
        } catch (IOException e) {
            System.out.println("An error occurred:" + e.getMessage());
        }
        return null;
    }
}
