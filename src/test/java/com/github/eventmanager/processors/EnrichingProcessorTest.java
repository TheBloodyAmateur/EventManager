package com.github.eventmanager.processors;

import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.OutputEntry;
import com.github.eventmanager.filehandlers.config.ProcessorEntry;
import com.github.eventmanager.formatters.EventCreator;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import static com.github.eventmanager.EventManagerTest.waitForEvents;
import static org.junit.jupiter.api.Assertions.*;

class EnrichingProcessorTest {

    @Test
    void processKV() {
        String expectedHost;
        String expectedIp = "";

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor(List.of("hostname", "ip"));
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

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor(List.of("hostname", "ip"));
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

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor(List.of("hostname", "ip"));
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


    @Test
    void testDifferentParameters() {
        String osName = "";
        String javaVersion = "";

        EnrichingProcessor enrichingProcessor = new EnrichingProcessor(List.of("osName", "javaVersion"));
        String event = new EventCreator("xml").lineNumber().create();
        int lineNumber = new Throwable().getStackTrace()[0].getLineNumber() - 1;

        String enrichedEvent = enrichingProcessor.processXML(event);

        try {
            osName = System.getProperty("os.name");
            javaVersion = System.getProperty("java.version");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String expected = "<event>" +
                "<lineNumber>"+lineNumber+"</lineNumber>" +
                "<osName>" + osName + "</osName>" +
                "<javaVersion>" + javaVersion + "</javaVersion>" +
                "</event>";

        assertEquals(expected, enrichedEvent);
    }

    @Test
    void addProcessorToEventManager() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            LogHandler logHandler = new LogHandler("");
            logHandler.getConfig().getEvent().setEventFormat("xml");

            ProcessorEntry processorEntry = new ProcessorEntry();
            processorEntry.setName("EnrichingProcessor");
            processorEntry.setParameters(Map.of("enrichingFields", List.of("osName", "javaVersion")));

            OutputEntry outputEntry = new OutputEntry();
            outputEntry.setName("PrintOutput");

            logHandler.getConfig().getProcessors().add(processorEntry);
            logHandler.getConfig().getOutputs().add(outputEntry);

            EventManager eventManager = new EventManager(logHandler);

            eventManager.logErrorMessage("This is an error message");
            waitForEvents();

            assertTrue(outContent.toString().contains("<osName>"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }
}