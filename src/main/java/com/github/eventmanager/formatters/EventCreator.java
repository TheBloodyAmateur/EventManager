package com.github.eventmanager.formatters;

import java.text.SimpleDateFormat;

/**
 * The EventCreator class is a builder class that creates event logs.
 * Contrary to the EventFormatter class, it can create event logs with a custom format. The format can be specified by
 * the user when creating an instance of the EventCreator class. The format can be one of the following: "json", "xml",
 * "csv", or "key-value".
 * <br><br>
 * The class includes information which can be found in the default format, such as the class name, method name, line
 * number, timestamp, level, exception, message, and arguments. These values are generated when creating an instance of
 * the EventCreator class, this should be kept in mind when creating events.
 *
 * */
public class EventCreator {
    private final StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
    private final String className = stackTraceElement[2].getClassName();
    private final String methodName = stackTraceElement[2].getMethodName();
    private final int lineNumber = stackTraceElement[2].getLineNumber();
    private final StringBuilder event = new StringBuilder();
    private final String eventFormat;
    private final EventFormatter formatter;
    private String formatSeperator = null;

    /**
     * The constructor of the EventCreator class.
     * @param format The format of the event log. The format can be one of the following: "json", "xml", "csv", or
     *               "key-value". If the format is not one of the specified formats, the default format is "key-value".
     * */
    public EventCreator(String format) {
        this.eventFormat = format;
        switch (format) {
            case "json":
                event.append("{");
                this.formatter = EventFormatter.JSON;
                this.formatSeperator = ",";
                break;
            case "xml":
                event.append("<event>");
                this.formatter = EventFormatter.XML;
                break;
            case "csv":
                this.formatter = EventFormatter.CSV;
                this.formatSeperator = ",";
                break;
            default:
                this.formatter = EventFormatter.KEY_VALUE;
                this.formatSeperator = " ";
                break;
        }
    }

    /**
     * Appends a key-value pair to the event. In case the format is "csv", only the value is appended.
     * @param key The key.
     * @param value The value .
     * */
    private void appendElement(String key, String value) {
        this.event.append(this.formatter.formatElement(new KeyValueWrapper(key, value)));
    }

    /**
     * Appends a list of key-value pairs to the event log. In case the format is "csv", only the values are appended.
     * @param args The list of key-value pairs.
     * */
    private void appendArguments(String body, KeyValueWrapper ... args) {
        this.event.append(this.formatter.formatArguments(body, args));
    }

    /**
     * Appends a separator to the event log.
     * */
    private void appendSeperator() {
        if(this.formatSeperator != null) {
            this.event.append(this.formatSeperator);
        }
    }

    /**
     * Appends the line number of the log to the event log.
     * @return The EventCreator object.
     * */
    public EventCreator lineNumber() {
        appendElement("lineNumber", String.valueOf(this.lineNumber));
        appendSeperator();
        return this;
    }

    /**
     * Appends the timestamp of the log to the event log.
     * @param timestampFormat The format of the timestamp. If the format is not valid, the current timestamp is used.
     *                        The format should be in the form of a pattern that can be used by the DateTimeFormatter
     * @return The EventCreator object.
     * */
    public EventCreator timestamp(String timestampFormat) {
        if(TimeStampFormatter.isValidTimeFormat(timestampFormat)) {
            String timestamp = (new SimpleDateFormat(timestampFormat)).format(System.currentTimeMillis() / 1000);
            appendElement("timestamp", timestamp);
        } else {
            String timestamp = (new SimpleDateFormat("dd.MM.yyyy h:mm:ss.SSS a z")).format(System.currentTimeMillis() / 1000);
            appendElement("timestamp", timestamp);
        }
        appendSeperator();
        return this;
    }

    /**
     * Appends the level of the log to the event log.
     * @param level The level of the log.
     * @return The EventCreator object.
     * */
    public EventCreator level(String level) {
        appendElement("level", level);
        appendSeperator();
        return this;
    }

    /**
     * Appends the class name of the log to the event log.
     * @return The EventCreator object.
     * */
    public EventCreator className() {
        appendElement("className", this.className);
        appendSeperator();
        return this;
    }

    /**
     * Appends the method name of the log to the event log.
     * @return The EventCreator object.
     * */
    public EventCreator methodName() {
        appendElement("methodName", this.methodName);
        appendSeperator();
        return this;
    }

    /**
     * Appends the exception to the event.
     * @param exception The exception of the log.
     * @return The EventCreator object.
     * */
    public EventCreator exception(String exception) {
        appendElement("exception", exception);
        appendSeperator();
        return this;
    }

    /**
     * Appends the message of the log to the event log.
     * @param message The message of the log.
     * @return The EventCreator object.
     * */
    public EventCreator message(String message) {
        appendElement("message", message);
        appendSeperator();
        return this;
    }

    /**
     * Appends the message of the log to the event log.
     * @param fieldName The field name of the log.
     * @param message The message of the log.
     * @return The EventCreator object.
     * */
    public EventCreator message(String fieldName, String message) {
        appendElement(fieldName, message);
        appendSeperator();
        return this;
    }

    /**
     * Appends the arguments of the log to the event log.
     * @param args The arguments of the log.
     * @return The EventCreator object.
     * */
    public EventCreator args(String args) {
        appendElement("args", args);
        appendSeperator();
        return this;
    }

    /**
     * Appends a key-value pairs o the log to the event log.
     * @param body The body name of the log.
     * @param args The key-value pair of the argument body.
     * @return The EventCreator object.
     * <br><br>
     * Example:
     * <pre>{@code
     *  EventCreator eventCreator = new EventCreator("json");
     *  eventCreator.args("arguments", new KeyValueWrapper);
     *  }</pre>
     * */
    public EventCreator args(String body, KeyValueWrapper args) {
        appendArguments(body, args);
        appendSeperator();
        return this;
    }

    /**
     * Appends a number of key-value pairs of the log to the event log.
     * @param body The body of the log.
     * @param args The key-value pair of the argument body.
     * @return The EventCreator object.
     * */
    public EventCreator args(String body, KeyValueWrapper ... args) {
        appendArguments(body, args);
        appendSeperator();
        return this;
    }

    /**
     * Appends the thread ID to the event.
     * @return The EventCreator object.
     * */
    public EventCreator threadID() {
        appendElement("threadID", String.valueOf(Thread.currentThread().getId()));
        appendSeperator();
        return this;
    }

    /**
     * Appends the thread name to the event.
     * @return The EventCreator object.
     * */
    public EventCreator threadName() {
        appendElement("threadName", Thread.currentThread().getName());
        appendSeperator();
        return this;
    }

    /**
     * Appends the hostname to the event log.
     * @return The EventCreator object.
     */
    public EventCreator hostname() {
        try {
            String hostname = java.net.InetAddress.getLocalHost().getHostName();
            appendElement("hostname", hostname);
        } catch (Exception e) {
            appendElement("hostname", "Unknown");
        }
        appendSeperator();
        return this;
    }

    /**
     * Appends the IP address to the event log.
     * @return The EventCreator object.
     */
    public EventCreator ipAddress() {
        try {
            String ipAddress = java.net.InetAddress.getLocalHost().getHostAddress();
            appendElement("ipAddress", ipAddress);
        } catch (Exception e) {
            appendElement("ipAddress", "Unknown");
        }
        appendSeperator();
        return this;
    }

    /**
     * Finalizes the event log and returns it as a string.
     * @return The event log as a string.
     * */
    public String create() {
        switch (eventFormat) {
            case "json":
                event.deleteCharAt(event.length() - 1);
                event.append("}");
                break;
            case "xml":
                event.append("</event>");
                break;
            case "csv":
                event.deleteCharAt(event.length() - 1);
                break;
            default:
                break;
        }
        return event.toString();
    }

    public EventCreator logLevel(String custom) {
        if (custom != null) {
            appendElement("logLevel", custom);
            appendSeperator();
        }
        return this;
    }
}
