package com.github.thebloodyamateur.eventmanager;

import com.github.thebloodyamateur.eventmanager.filehandlers.ConfigHandler;
import com.github.thebloodyamateur.eventmanager.filehandlers.LogHandler;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.github.thebloodyamateur.eventmanager.formatters.TimeStampFormatter;

public class EventManager {
    @Setter
    @Getter
    private String timeFormat;
    private LogHandler logHandler;

    public EventManager(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    public static String setCorrectOSSeperator(String path){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = path.replace("/", "\\");
        }
        return path;
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

//        try {
//            // Check if the file exists and create it if it doesn't
//            if(!new File(this.logHandler.getFileName()).exists()){
//                this.logHandler.getFileName() = String.valueOf(new File(this.logHandler.getFileName()).createNewFile());
//            }
//            // Write the event to the file
//            FileWriter myWriter = new FileWriter(this.getFileName(), true);
//            myWriter.write(String.format("[%s] %s %s %s %d: %s\n", time, level, callerClassName, callerMethodName, lineNumber, message));
//            myWriter.close();
//        } catch (IOException e) {
//            System.out.println("An error occurred:"+e.getMessage());
//        }
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
        if(this.logHandler.isInformationalModeOn() || this.logHandler.isDebugModeOn()){
            logMessage("INFO", exception);
        }
    }

    public void logDebugMessage(Object exception) {
        if(this.logHandler.isDebugModeOn()){
            logMessage("DEBUG", exception);
        }
    }
}
