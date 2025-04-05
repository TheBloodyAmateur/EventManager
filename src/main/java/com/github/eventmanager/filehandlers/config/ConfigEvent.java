package com.github.eventmanager.filehandlers.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The ConfigEvent class holds the configuration settings related to events for the EventManager application.
 * It includes settings for printing logs to the console, saving logs to a file, debugging mode, informational mode,
 * time format for logging events, and event format for logging events.
 */
public class ConfigEvent {
    /**
     * @deprecated This feature was deprecated, with {@link OutputEntry} being the new method.
     * */
    private final AtomicBoolean printToConsole = new AtomicBoolean(false);
    /**
     * @deprecated This feature was deprecated, with {@link OutputEntry} being the new method.
     * */
    private final AtomicBoolean printAndSaveToFile = new AtomicBoolean(false);
    private final AtomicBoolean debuggingMode = new AtomicBoolean(false);
    private final AtomicBoolean informationalMode = new AtomicBoolean(false);
    private final AtomicReference<String> timeFormat = new AtomicReference<>("dd.MM.yyyy h:mm:ss.SSS a z");
    private final AtomicReference<String> eventFormat = new AtomicReference<>("default");

    /**
     * Gets whether logs should be printed to the console.
     *
     * @return true if logs should be printed to the console, false otherwise.
     *
     * @deprecated This feature was deprecated, with {@link OutputEntry} being the new method.
     */
    public boolean getPrintToConsole() {
        return printToConsole.get();
    }

    /**
     * Sets whether logs should be printed to the console.
     *
     * @param printToConsole true to print logs to the console, false otherwise.
     *
     * @deprecated This feature was deprecated, with {@link OutputEntry} being the new method.
     */
    public void setPrintToConsole(boolean printToConsole) {
        this.printToConsole.set(printToConsole);
    }

    /**
     * Gets whether logs should be printed and saved to a file.
     *
     * @return true if logs should be printed and saved to a file, false otherwise.
     *
     * @deprecated This feature was deprecated, with {@link OutputEntry} being the new method.
     */
    public boolean getPrintAndSaveToFile() {
        return printAndSaveToFile.get();
    }

    /**
     * Sets whether logs should be printed and saved to a file.
     *
     * @param printAndSaveToFile true to print and save logs to a file, false otherwise.
     *
     * @deprecated This feature was deprecated, with {@link OutputEntry} being the new method.
     */
    public void setPrintAndSaveToFile(boolean printAndSaveToFile) {
        this.printAndSaveToFile.set(printAndSaveToFile);
    }

    /**
     * Gets whether debugging mode is enabled.
     *
     * @return true if debugging mode is enabled, false otherwise.
     */
    public boolean getDebuggingMode() {
        return debuggingMode.get();
    }

    /**
     * Sets whether debugging mode is enabled.
     *
     * @param debuggingMode true to enable debugging mode, false otherwise.
     */
    public void setDebuggingMode(boolean debuggingMode) {
        this.debuggingMode.set(debuggingMode);
    }

    /**
     * Gets whether informational mode is enabled.
     *
     * @return true if informational mode is enabled, false otherwise.
     */
    public boolean getInformationalMode() {
        return informationalMode.get();
    }

    /**
     * Sets whether informational mode is enabled.
     *
     * @param informationalMode true to enable informational mode, false otherwise.
     */
    public void setInformationalMode(boolean informationalMode) {
        this.informationalMode.set(informationalMode);
    }

    /**
     * Gets the time format for logging events.
     *
     * @return the time format for logging events.
     */
    public String getTimeFormat() {
        return timeFormat.get();
    }

    /**
     * Sets the time format for logging events.
     *
     * @param timeFormat the time format for logging events.
     */
    public void setTimeFormat(String timeFormat) {
        this.timeFormat.set(timeFormat);
    }

    /**
     * Gets the event format for logging events.
     *
     * @return the event format for logging events.
     */
    public String getEventFormat() {
        return eventFormat.get();
    }

    /**
     * Sets the event format for logging events.
     *
     * @param eventFormat the event format for logging events.
     */
    public void setEventFormat(String eventFormat) {
        this.eventFormat.set(eventFormat);
    }
}