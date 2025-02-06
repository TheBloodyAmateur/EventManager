package io.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigEvent {
    private AtomicBoolean printToConsole = new AtomicBoolean(false);
    private AtomicBoolean printAndSaveToFile = new AtomicBoolean(false);
    private AtomicBoolean debuggingMode = new AtomicBoolean(false);
    private AtomicBoolean informationalMode = new AtomicBoolean(false);
    private AtomicReference<String> timeFormat = new AtomicReference<>("dd.MM.yyyy h:mm:ss.SSS a z");
    private AtomicReference<String> eventFormat = new AtomicReference<>("default");

    public boolean getPrintToConsole() {
        return printToConsole.get();
    }

    public void setPrintToConsole(boolean printToConsole) {
        this.printToConsole.set(printToConsole);
    }

    public boolean getPrintAndSaveToFile() {
        return printAndSaveToFile.get();
    }

    public void setPrintAndSaveToFile(boolean printAndSaveToFile) {
        this.printAndSaveToFile.set(printAndSaveToFile);
    }

    public boolean getDebuggingMode() {
        return debuggingMode.get();
    }

    public void setDebuggingMode(boolean debuggingMode) {
        this.debuggingMode.set(debuggingMode);
    }

    public boolean getInformationalMode() {
        return informationalMode.get();
    }

    public void setInformationalMode(boolean informationalMode) {
        this.informationalMode.set(informationalMode);
    }

    public String getTimeFormat() {
        return timeFormat.get();
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat.set(timeFormat);
    }

    public String getEventFormat() {
        return eventFormat.get();
    }

    public void setEventFormat(String eventFormat) {
        this.eventFormat.set(eventFormat);
    }
}
