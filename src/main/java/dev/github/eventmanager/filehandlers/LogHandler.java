package dev.github.eventmanager.filehandlers;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogHandler {
    @Getter
    @Setter
    public String currentFileName;
    @Getter
    public String configHandlerFileName;
    @Getter
    boolean debugModeOn = false;
    @Getter
    boolean informationalModeOn = false;
    @Getter
    private String timeFormat;
    @Getter
    public String filePath;
    @Getter
    private int maxSizeInKB;
    private int rotationSize;
    private int rotationPeriodInSeconds;
    private long lastRotationTime;

    public LogHandler(ConfigHandler configHandler) {
        this.filePath = configHandler.filePath;
        this.configHandlerFileName = configHandler.fileName;
        this.currentFileName = this.createNewFileName(configHandler.fileName, configHandler.fileExtension);
        this.rotationSize = configHandler.maxSizeInKB;
        this.maxSizeInKB = configHandler.maxSizeInKB;
        this.rotationPeriodInSeconds = configHandler.rotationPeriodInSeconds;
        this.debugModeOn = configHandler.debugModeOn;
        this.informationalModeOn = configHandler.informationalModeOn;
        this.timeFormat = configHandler.timeFormat;
    }

    private String createNewFileName(String fileName, String fileExtension) {
        long lastRotationTime = System.currentTimeMillis()/1000;
        return fileName + "-" + lastRotationTime + fileExtension;
    }

    public void checkIfLogFileNeedsRotation(){
        // TODO Check if the log file needs rotation

        // Get the size of the file in KB
        //long sizeKB = new File(this.filePath).length()/1024;

        // Get all log files in the current directory
        File directory = new File(this.getFilePath());
        File[] files = directory.listFiles();

        // Create a pattern to match the log file names and extract the timestamp
        Pattern pattern = Pattern.compile(this.getConfigHandlerFileName()+"-(?<fileTimeStamp>[0-9]+).log");

        try{
            for (File file : files) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()){
                    // Extract the timestamp from the file name and compare it to the current time
//                    long fileTimeStamp = Long.parseLong(matcher.group("fileTimeStamp"));
//                    long currentTime = System.currentTimeMillis()/1000L;

                    FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                    long fileTimeStamp = creationTime.toMillis()/1000L;
                    long currentTime = System.currentTimeMillis()/1000L;

                    if((currentTime - fileTimeStamp) > this.rotationPeriodInSeconds){
                        // Rotate the log file
                        this.rotateLgFile(file);
                    }
                }
            }
        } catch (Exception e){
            System.out.println("An error occurred: "+e.getMessage());
        }

    }

    public void rotateLgFile(File file){
        // TODO Rotate the log file
        System.out.println("Rotating log file: "+file.getName());
    }

    public boolean checkIfLogFileExists(){
        return new File(this.filePath).exists();
    }

    public void createLogFile(){
        try {
            this.setCurrentFileName(String.valueOf(new File(this.getCurrentFileName()).createNewFile()));
        } catch (IOException e) {
            System.out.println("An error occurred: "+e.getMessage());
        }
    }

    /**
     * Read the log file and return its content as a list of strings.
     *
     * @return The content of the log file as a list of strings.
     * @throws IOException On input error.
     */
    public List<String> readLogFile(){
        BufferedReader reader;
        try {
            // Read the file and return its content as a list of strings
            reader = new BufferedReader(new FileReader(this.filePath));
            return reader.lines().toList();
        } catch (IOException e) {
            System.out.println("An error occurred:"+e.getMessage());
        }
        return null;
    }
}
