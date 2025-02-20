package com.github.eventmanager.formatters;

/**
 * The EventCreator class is a builder class that creates event logs.
 * Contrary to the EventFormatter class, it can create event logs with a custom format.
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

    private void appendElement(String key, String value) {
        this.event.append(this.formatter.formatElement(new KeyValueWrapper(key, value)));
    }

    private void appendArguments(KeyValueWrapper ... args) {
        this.event.append(this.formatter.formatArguments(args));
    }

    private void appendSeperator() {
        if(this.formatSeperator != null) {
            this.event.append(this.formatSeperator);
        }
    }

    public EventCreator lineNumber() {
        appendElement("lineNumber", String.valueOf(this.lineNumber));
        appendSeperator();
        return this;
    }

    public EventCreator timestamp(String timestampFormat) {
        appendElement("timestamp", timestampFormat);
        appendSeperator();
        return this;
    }

    public EventCreator level(String level) {
        appendElement("level", level);
        appendSeperator();
        return this;
    }

    public EventCreator className() {
        appendElement("className", this.className);
        appendSeperator();
        return this;
    }

    public EventCreator methodName() {
        appendElement("methodName", this.methodName);
        appendSeperator();
        return this;
    }

    public EventCreator exception(String exception) {
        appendElement("exception", exception);
        appendSeperator();
        return this;
    }

    public EventCreator message(String message) {
        appendElement("message", message);
        appendSeperator();
        return this;
    }

    public EventCreator args(String args) {
        appendElement("args", args);
        appendSeperator();
        return this;
    }

    public EventCreator args(KeyValueWrapper args) {
        appendArguments(args);
        appendSeperator();
        return this;
    }

    public EventCreator args(KeyValueWrapper ... args) {
        appendArguments(args);
        appendSeperator();
        return this;
    }

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
}
