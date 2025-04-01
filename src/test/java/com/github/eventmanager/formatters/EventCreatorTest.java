package com.github.eventmanager.formatters;

import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventCreatorTest {

    @Test
    void testJSONEventOne() {
        String eventCreator = new EventCreator("json").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("{\"lineNumber\": \"" + lineNumber + "\"}", eventCreator);
    }

    @Test
    void testJSONEventTwo() {
        String eventCreator = new EventCreator("json").lineNumber().message("Hello, World!").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"message\": \"Hello, World!\"}", eventCreator);
    }

    @Test
    void testJSONEventThree() {
        String eventCreator = new EventCreator("json").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"message\": \"Hello, World!\",\"args\": {\"args\":\"arg1\"}}", eventCreator);
    }

    @Test
    void testJSONCustomLogLevel() {
        String eventCreator = new EventCreator("json").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).level("CUSTOM").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"message\": \"Hello, World!\",\"args\": {\"args\":\"arg1\"},\"logLevel\": \"CUSTOM\"}", eventCreator);
    }

    @Test
    void testJSONInfoLogLevel() {
        String eventCreator = new EventCreator("json").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).level("INFO").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"message\": \"Hello, World!\",\"args\": {\"args\":\"arg1\"},\"logLevel\": \"INFO\"}", eventCreator);
    }

    @Test
    void testXMLEventOne() {
        String eventCreator = new EventCreator("xml").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("<event><lineNumber>" + lineNumber + "</lineNumber></event>", eventCreator);
    }

    @Test
    void testXMLEventTwo() {
        String eventCreator = new EventCreator("xml").lineNumber().message("Hello, World!").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("<event><lineNumber>" + lineNumber + "</lineNumber><message>Hello, World!</message></event>", eventCreator);
    }

    @Test
    void testXMLEventThree() {
        String eventCreator = new EventCreator("xml").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("<event><lineNumber>" + lineNumber + "</lineNumber><message>Hello, World!</message><args><args>arg1</args></args></event>", eventCreator);
    }

    @Test
    void testXMLCustomLogLevel() {
        String eventCreator = new EventCreator("xml").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).level("CUSTOM").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("<event><lineNumber>" + lineNumber + "</lineNumber><message>Hello, World!</message><args><args>arg1</args></args><logLevel>CUSTOM</logLevel></event>", eventCreator);
    }

    @Test
    void testXMLInfoLogLevel() {
        String eventCreator = new EventCreator("xml").lineNumber().message("Hello, World!").args("args",new KeyValueWrapper("args", "arg1")).level("INFO").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("<event><lineNumber>" + lineNumber + "</lineNumber><message>Hello, World!</message><args><args>arg1</args></args><logLevel>INFO</logLevel></event>", eventCreator);
    }

    @Test
    void testCSVEventOne() {
        String eventCreator = new EventCreator("csv").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals(String.valueOf(lineNumber), eventCreator);
    }

    @Test
    void testCSVEventTwo() {
        String eventCreator = new EventCreator("csv").lineNumber().message("Hello World!").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals(lineNumber + ",Hello World!", eventCreator);
    }

    @Test
    void testCSVEventThree() {
        String eventCreator = new EventCreator("csv").lineNumber().message("Hello World!").args("args",new KeyValueWrapper("args", "arg1")).create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals(lineNumber + ",Hello World!,arg1", eventCreator);
    }

    @Test
    void testCSVCustomLogLevel() {
        String eventCreator = new EventCreator("csv").lineNumber().message("Hello World!").args("args",new KeyValueWrapper("args", "arg1")).level("CUSTOM").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals(lineNumber + ",Hello World!,arg1,CUSTOM", eventCreator);
    }

    @Test
    void testCSVInfoLogLevel() {
        String eventCreator = new EventCreator("csv").lineNumber().message("Hello World!").args("args",new KeyValueWrapper("args", "arg1")).level("INFO").create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals(lineNumber + ",Hello World!,arg1,INFO", eventCreator);
    }

    @Test
    void testHostName() {
        String eventCreator = new EventCreator("json").lineNumber().hostname().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        try {
            String hostname = java.net.InetAddress.getLocalHost().getHostName();
            assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"hostname\": \"" + hostname + "\"}", eventCreator);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIPAddress() {
        String eventCreator = new EventCreator("json").lineNumber().ipAddress().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        try {
            String ipAddress = java.net.InetAddress.getLocalHost().getHostAddress();
            assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"ipAddress\": \"" + ipAddress + "\"}", eventCreator);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testThreadName() {
        String eventCreator = new EventCreator("json").lineNumber().threadName().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        assertEquals("{\"lineNumber\": \"" + lineNumber + "\",\"threadName\": \"" + Thread.currentThread().getName() + "\"}", eventCreator);
    }
}