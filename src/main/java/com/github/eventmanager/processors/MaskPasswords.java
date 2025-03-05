package com.github.eventmanager.processors;

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
