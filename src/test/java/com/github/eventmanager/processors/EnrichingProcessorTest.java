package com.github.eventmanager.processors;

import com.github.eventmanager.formatters.EventCreator;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

class EnrichingProcessorTest {

    @Test
    void processKV() {
        String expectedHost;
        String expectedIp = "";

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor();
        String event = new EventCreator("kv").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;
        String enrichedEvent = enrichingProcessor.processKV(event);

        try {
            expectedIp = InetAddress.getLocalHost().getHostAddress();
            expectedHost = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            expectedHost = "Unknown";
        }
        String expected = "lineNumber=\""+lineNumber+"\" " +
                "hostname=\"" + expectedHost +"\" ip=\"" + expectedIp + "\" ";

        assertEquals(expected, enrichedEvent);
    }

    @Test
    void processJSON() {
        String expectedHost;
        String expectedIp = "";

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor();
        String event = new EventCreator("json").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;

        String enrichedEvent = enrichingProcessor.processJSON(event);

        try {
            expectedIp = InetAddress.getLocalHost().getHostAddress();
            expectedHost = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            expectedHost = "Unknown";
        }

        String expected = "{\"lineNumber\": \""+lineNumber+"\",\"hostname\":\"" +
                expectedHost + "\",\"ip\":\"" + expectedIp + "\"}";

        assertEquals(expected, enrichedEvent);
    }

    @Test
    void processXML() {
        String expectedHost;
        String expectedIp = "";

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor();
        String event = new EventCreator("xml").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;

        String enrichedEvent = enrichingProcessor.processXML(event);

        try {
            expectedIp = InetAddress.getLocalHost().getHostAddress();
            expectedHost = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            expectedHost = "Unknown";
        }

        String expected = "<event><lineNumber>"+lineNumber+"</lineNumber><hostname>" + expectedHost +
                "</hostname><ip>" + expectedIp + "</ip></event>";

        assertEquals(expected, enrichedEvent);
    }
}