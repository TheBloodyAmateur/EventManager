package com.github.thebloodyamateur.eventmanager.filehandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LogHandler {
    private String filePath = "/var/log/EventManager.log";
    private int rotationSize;
    private int rotationCount = 0;
    private int rotationPeriodInSeconds = 86400;


    public LogHandler(String filePath){
        this.filePath = filePath;
    }

    public LogHandler(String filePath, int rotationSize){
        this.filePath = filePath;
        this.rotationSize = rotationSize;
    }

    public LogHandler(String filePath, int rotationSize, int rotationPeriodInSeconds){
        this.filePath = filePath;
        this.rotationSize = rotationSize;
        this.rotationPeriodInSeconds = rotationPeriodInSeconds;
    }

    public LogHandler(String filePath, int rotationSize, int rotationPeriodInSeconds, int rotationCount){
        this.filePath = filePath;
        this.rotationSize = rotationSize;
        this.rotationPeriodInSeconds = rotationPeriodInSeconds;
        this.rotationCount = rotationCount;
    }

    public void rotateLgFile(){
        // TODO Rotate the log file
    }

    public void createNewFileName(){
        // TODO Create a new file name with a timestamp appended to it
    }



    /**
     * Read the log file and return its content as a list of strings.
     *
     * @return The content of the log file as a list of strings.
     * @thows IOException On input error.
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
