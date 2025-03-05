package com.github.eventmanager.processors;

public interface Processor {
    String processKV(String event);

    String processJSON(String event);

    String processXML(String event);
}
