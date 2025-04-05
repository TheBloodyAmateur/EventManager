package com.github.eventmanager.processors;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FilterProcessorTest {
    FilterProcessor filterProcessor;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        filterProcessor = new FilterProcessor(Collections.singletonList("test"));
    }

    @org.junit.jupiter.api.Test
    void testProcessKV() {
        String event = "This is a test event";
        String result = filterProcessor.processKV(event);
        assertEquals("", result);
    }

    @org.junit.jupiter.api.Test
    void testProcessJSON() {
        String event = "{\"event\": \"This is a test event\"}";
        String result = filterProcessor.processJSON(event);
        assertEquals("", result);
    }

    @org.junit.jupiter.api.Test
    void testProcessXML() {
        String event = "<event>This is a test event</event>";
        String result = filterProcessor.processXML(event);
        assertEquals("", result);
    }

}