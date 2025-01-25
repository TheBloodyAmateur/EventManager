package com.github.thebloodyamateur.eventmanager;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventManager {
    @Getter
    @Setter
    private String fileName;
    @Setter
    @Getter
    private String timeFormat;
    public List<String> availableTimeFormats;
    private boolean debugModeOn = false;
    private boolean informationalModeOn = false;

    public EventManager(String fileName, String timeFormat) {
        this.fileName = fileName;
        if(isValidTimeFormat(timeFormat)){
            this.timeFormat = timeFormat;
        } else {
            this.timeFormat = "dd-MMM-yyyy HH:mm:ss z";
        }
        readConfigFile();
    }

    public EventManager(String fileName) {
        this.fileName = fileName;
        readConfigFile();
        this.timeFormat = availableTimeFormats.get(0);
    }

    public EventManager() {
        this.fileName = setCorrectOSSeperator("/tmp/richAF.log");
        readConfigFile();
        this.timeFormat = availableTimeFormats.get(0);
    }

    private String setCorrectOSSeperator(String path){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = path.replace("/", "\\");
        }
        return path;
    }

    private void readConfigFile() {
        JSONObject config;
        List<String> formats = new ArrayList<String>();
        List<String> replacement = Arrays.asList("dd-MMM-yyyy HH:mm:ss z", "dd.MM.yyyy h:mm:ss.SSS a z", "E, MMM dd yyyy HH:mm:ss z");

        // Get the path of the file and decode it to UTF-8 to cope with special characters
        String configPath = setCorrectOSSeperator("config/loggingConfig.json");
        String path = System.getProperty("user.dir")+File.separator+configPath;
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Setting a default time format in case of an error
        this.timeFormat = replacement.get(0);

        // Error handling of the file processing part
        try {
            FileReader fileReader = new FileReader(path);
            config = new JSONObject(new JSONTokener(fileReader));
        } catch (Exception e) {
            logErrorMessage(e);
            this.availableTimeFormats = replacement;
            return;
        }

        // Error handling of the time format validation
        try{
            for(Object format : config.getJSONArray("timestamps")){
                if(isValidTimeFormat((String) format)){
                    formats.add((String) format);
                }
            }
        } catch (Exception e){
            logErrorMessage(e);
            this.availableTimeFormats = replacement;
            return;
        }

        try {
            debugModeOn = config.getBoolean("debuggingMode");
            informationalModeOn = config.getBoolean("informationalMode");
        } catch (Exception e) {
            logWarningMessage(e);
            debugModeOn = false;
            informationalModeOn = false;
        }

        this.availableTimeFormats = formats;
    }

    private boolean isValidTimeFormat(String timeFormat) {
        try {
            DateTimeFormatter.ofPattern(timeFormat);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
    * Log a message to the destination file.
     *
     * @param level The log level of the message.
     * @param message The message to log.
     *
     * @thows IOException On input error.
    * */
    private void logMessage(String level, Object message) {
        if (message instanceof Exception) {
            message = ((Exception) message).getMessage();
        } else if (message instanceof String) {
            message = message.toString();
        } else {
            message = "Unknown error";
        }

        // Get the class name and method of the caller
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String callerClassName = stackTraceElement[3].getClassName();
        String callerMethodName = stackTraceElement[3].getMethodName();
        int lineNumber = stackTraceElement[3].getLineNumber();

        // Get the current time as a string in the specified format
        String time = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(this.timeFormat));

        try {
            // Check if the file exists and create it if it doesn't
            if(!new File(this.fileName).exists()){
                this.fileName = String.valueOf(new File(this.fileName).createNewFile());
            }
            // Write the event to the file
            FileWriter myWriter = new FileWriter(this.fileName, true);
            myWriter.write(String.format("[%s] %s %s %s %d: %s\n", time, level, callerClassName, callerMethodName, lineNumber, message));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred:"+e.getMessage());
        }
    }

    public void logFatalMessage(Object exception) {
        logMessage("FATAL", exception);
    }

    public void logErrorMessage(Object exception) {
        logMessage("ERROR", exception);
    }

    public void logWarningMessage(Object exception) {
        logMessage("WARNING", exception);
    }

    public void logInfoMessage(Object exception) {
        if(informationalModeOn || debugModeOn){
            logMessage("INFO", exception);
        }
    }

    public void logDebugMessage(Object exception) {
        if(debugModeOn){
            logMessage("DEBUG", exception);
        }
    }
}
