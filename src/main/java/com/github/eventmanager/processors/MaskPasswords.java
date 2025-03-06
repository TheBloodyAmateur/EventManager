package com.github.eventmanager.processors;

/**
 * The MaskPasswords class is responsible for masking passwords in event data.
 * */
public class MaskPasswords implements Processor {
    @Override
    public String processKV(String event) {
        return event.replaceAll("password=\"\\w+\"", "password=***");
    }

    @Override
    public String processJSON(String event) {
        return event.replaceAll("\"password\":\\s+\"\\w+\"", "\"password\": \"***\"");
    }

    @Override
    public String processXML(String event) {
        return event.replaceAll("<password>\\w+</password>", "<password>***</password>");
    }
}
