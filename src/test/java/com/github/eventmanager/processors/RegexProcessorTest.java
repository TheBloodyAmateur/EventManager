package com.github.eventmanager.processors;

import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.ProcessorEntry;
import com.github.eventmanager.filehandlers.config.RegexEntry;
import com.github.eventmanager.formatters.EventCreator;
import com.github.eventmanager.formatters.KeyValueWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static com.github.eventmanager.EventManagerTest.waitForEvents;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexProcessorTest {

    private RegexProcessor regexProcessor;

    @BeforeEach
    public void setUp() {
        RegexEntry entry1 = new RegexEntry("field1", "value1", "replacement1");
        RegexEntry entry2 = new RegexEntry("field2", "value2", "replacement2");
        regexProcessor = new RegexProcessor(List.of(entry1, entry2));
    }

    @Test
    public void testProcessKV() {
        String event = "field1=\"value1\" field2=\"value2\"";
        String expected = "field1=\"replacement1\" field2=\"replacement2\"";
        String result = regexProcessor.processKV(event);
        assertEquals(expected, result);
    }

    @Test
    public void testProcessJSON() {
        String event = "{\"field1\": \"value1\", \"field2\": \"value2\"}";
        String expected = "{\"field1\": \"replacement1\", \"field2\": \"replacement2\"}";
        String result = regexProcessor.processJSON(event);
        assertEquals(expected, result);
    }

    @Test
    public void testProcessXML() {
        String event = "<field1>value1</field1><field2>value2</field2>";
        String expected = "<field1>replacement1</field1><field2>replacement2</field2>";
        String result = regexProcessor.processXML(event);
        assertEquals(expected, result);
    }

    @Test
    void addProcessorToEventManager() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try{
            LogHandler logHandler = new LogHandler("");
            logHandler.getConfig().getEvent().setEventFormat("xml");
            logHandler.getConfig().getEvent().setPrintToConsole(true);

            ProcessorEntry processorEntry = new ProcessorEntry();
            processorEntry.setName("RegexProcessor");
            processorEntry.setParameters(Map.of("regexEntries",
                    List.of(new RegexEntry("field1", "value1", "replacement1"))));

            logHandler.getConfig().getProcessors().add(processorEntry);

            EventManager eventManager = new EventManager(logHandler);
            String event = new EventCreator("xml").message("field1","value1").create();
            eventManager.logErrorMessage(event);
            waitForEvents();

            assertTrue(outContent.toString().contains("<field1>replacement1</field1>"));
        } catch (Exception e) {
            // Clean up: Reset System.out
            System.setOut(originalOut);
            throw new RuntimeException(e);
        }
    }
}