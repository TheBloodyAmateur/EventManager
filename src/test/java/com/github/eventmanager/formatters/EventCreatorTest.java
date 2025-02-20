package com.github.eventmanager.formatters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventCreatorTest {

    @Test
    void testJSONEventOne() {
        String eventCreator = new EventCreator("json").lineNumber().create();
        assertEquals("{\"lineNumber\": \"11\"}", eventCreator);
    }

    @Test
    void testJSONEventTwo() {
        String eventCreator = new EventCreator("json").lineNumber().message("Hello, World!").create();
        assertEquals("{\"lineNumber\": \"17\",\"message\": \"Hello, World!\"}", eventCreator);
    }

    @Test
    void testJSONEventThree() {
        String eventCreator = new EventCreator("json").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).create();
        assertEquals("{\"lineNumber\": \"23\",\"message\": \"Hello, World!\",\"args\": {\"args\":\"arg1\"}}", eventCreator);
    }

    @Test
    void testXMLEventOne() {
        String eventCreator = new EventCreator("xml").lineNumber().create();
        assertEquals("<event><lineNumber>29</lineNumber></event>", eventCreator);
    }

    @Test
    void testXMLEventTwo() {
        String eventCreator = new EventCreator("xml").lineNumber().message("Hello, World!").create();
        assertEquals("<event><lineNumber>35</lineNumber><message>Hello, World!</message></event>", eventCreator);
    }

    @Test
    void testXMLEventThree() {
        String eventCreator = new EventCreator("xml").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).create();
        assertEquals("<event><lineNumber>41</lineNumber><message>Hello, World!</message><args><args>arg1</args></args></event>", eventCreator);
    }

    @Test
    void testCSVEventOne() {
        String eventCreator = new EventCreator("csv").lineNumber().create();
        assertEquals("47", eventCreator);
    }

    @Test
    void testCSVEventTwo() {
        String eventCreator = new EventCreator("csv").lineNumber().message("Hello World!").create();
        assertEquals("53,Hello World!", eventCreator);
    }

    @Test
    void testCSVEventThree() {
        String eventCreator = new EventCreator("csv").lineNumber().message("Hello World!").args("args",new KeyValueWrapper("args", "arg1")).create();
        assertEquals("59,Hello World!,arg1", eventCreator);
    }
}