package com.github.eventmanager.processors;

import static org.junit.jupiter.api.Assertions.*;
class SampleProcessorTest {
    SampleProcessor sampleProcessor;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        sampleProcessor = new SampleProcessor(5);
    }

    @org.junit.jupiter.api.Test
    void testProcessKV() {
        String keyValueEvent = "key=value";
        String result = sampleProcessor.processKV(keyValueEvent);
        assertEquals("", result);
    }

    @org.junit.jupiter.api.Test
    void testProcessJSON() {
        String event = "{\"event\": \"This is a test event\"}";
        String result = sampleProcessor.processJSON(event);
        assertEquals("", result);
    }

    @org.junit.jupiter.api.Test
    void testProcessXML() {
        String event = "<event>This is a test event</event>";
        String result = sampleProcessor.processXML(event);
        assertEquals("", result);
    }

    @org.junit.jupiter.api.Test
    void testProcessText(){
        String json = "{\"event\": \"This is a test event\"}";
        String event = "";

        for (int i = 0; i <= 5; i++) {
            event = sampleProcessor.processJSON(json);
        }

        assertEquals(json, event);
    }
  
}