package at.ac.fhcampuswien.richAF.data;

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
    private String fileName;
    private String timeFormat;
    public List<String> availableTimeFormats;
    public boolean debugModeOn = false;
    public boolean informationalModeOn = false;

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
        this.fileName = "/tmp/richAF.log";
        readConfigFile();
        this.timeFormat = availableTimeFormats.get(0);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    private void readConfigFile() {
        JSONObject config;
        List<String> formats = new ArrayList<String>();
        List<String> replacement = Arrays.asList("dd-MMM-yyyy HH:mm:ss z", "dd.MM.yyyy h:mm:ss.SSS a z", "E, MMM dd yyyy HH:mm:ss z");

        // Get the path of the file and decode it to UTF-8 to cope with special characters
        String path = ClassLoader.getSystemResource("loggingConfig.json").getPath();
        path = java.net.URLDecoder.decode(path, java.nio.charset.StandardCharsets.UTF_8);

        // Setting a default time format in case of an error
        this.timeFormat = replacement.get(0);

        // Error handling of the file processing part
        try {
            FileReader fileReader = new FileReader(path);
            config = new JSONObject(new JSONTokener(fileReader));
        } catch (Exception e) {
            logErrorMessage("An error occurred:"+e.getMessage());
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
            logErrorMessage("An error occurred:"+e.getMessage());
            this.availableTimeFormats = replacement;
            return;
        }

        try {
            debugModeOn = config.getBoolean("debugMode");
            informationalModeOn = config.getBoolean("informationalMode");
        } catch (Exception e) {
            logWarningMessage("Invalid value for debugMode or informationalMode in the config file. Using default value.");
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
    private void logMessage(String level, String message) {
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

    public void logFatalMessage(String message) {
        logMessage("FATAL", message);
    }

    public void logErrorMessage(String message) {
        logMessage("ERROR", message);
    }

    public void logWarningMessage(String message) {
        logMessage("WARNING", message);
    }

    public void logInfoMessage(String message) {
        if(informationalModeOn || debugModeOn){
            logMessage("INFO", message);
        }
    }

    public void logDebugMessage(String message) {
        if(debugModeOn){
            logMessage("DEBUG", message);
        }
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
            reader = new BufferedReader(new FileReader(this.fileName));
            return reader.lines().toList();
        } catch (IOException e) {
            System.out.println("An error occurred:"+e.getMessage());
        }
        return null;
    }
}
