package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.OutputEntry;
import com.github.eventmanager.filehandlers.config.ProcessorEntry;
import com.github.eventmanager.filehandlers.config.RegexEntry;
import com.github.eventmanager.filehandlers.config.SocketEntry;
import com.github.eventmanager.formatters.KeyValueWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventManagerTest {
    String configPath = "config/loggingConfig.json";
    private EventManager eventManager;

    // Wait for events to be logged because the event thread is asynchronous
    public static void waitForEvents() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        eventManager.stopPipeline();
    }

    @Test
    void createInstance() {
        LogHandler logHandler = new LogHandler(configPath);
        this.eventManager = new EventManager(logHandler);
    }

    @Test
    void createDefaultEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("default");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("This is an informational message");
        eventManager.logWarningMessage("This is an error message");
        eventManager.logFatalMessage("This is a fatal message");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is an informational message")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is an error message")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is a fatal message")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createDefaultEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("default");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logWarningMessage(new KeyValueWrapper("gisela", "brünhilde"), new KeyValueWrapper("detlef", "herzig"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("gisela=\"brünhilde\"")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("detlef=\"herzig\"")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createKVEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("kv");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("key=\"value\"")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createKVEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("key=\"value\"")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createCSVEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("csv");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage("test3");

        // Check if the log file exists
        System.out.println(logHandler.checkIfLogFileExists());
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("test3")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createCSVEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("csv");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("vader,skywalker")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createXMLEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("xml");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage("test3");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("<message>test3</message>")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createXMLEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("xml");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logFatalMessage(new KeyValueWrapper("darth", "vader"), new KeyValueWrapper("luke", "skywalker"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("<darth>vader</darth><luke>skywalker</luke>")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createJSONEvents() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("json");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("test1");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"message\":\"test1\"")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void createJSONEventsWithArguments() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("json");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage(new KeyValueWrapper("key", "value"), new KeyValueWrapper("value", "key"));

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("\"key\":\"value\"")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void consoleOutput() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            LogHandler logHandler = new LogHandler(configPath, true);

            OutputEntry outputEntry = new OutputEntry();
            outputEntry.setName("PrintOutput");
            logHandler.getConfig().getOutputs().add(outputEntry);

            this.eventManager = new EventManager(logHandler);
            eventManager.logErrorMessage("This is an error message");

            // Check if the console output contains the error message
            waitForEvents();
            assertTrue(outContent.toString().contains("This is an error message"));
        } finally {
            // Clean up: Reset System.out
            System.setOut(originalOut);
        }
    }

    @Test
    void addOutputAndVerifyOutput() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Start mock socket server
            Future<String> receivedEvent = executor.submit(() -> {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    return reader.readLine(); // read a single event line
                }
            });
            LogHandler logHandler = new LogHandler(configPath, true);

            OutputEntry outputEntry = new OutputEntry();
            outputEntry.setName("PrintOutput");
            logHandler.getConfig().getOutputs().add(outputEntry);

            this.eventManager = new EventManager(logHandler);

            OutputEntry outputEntry2 = new OutputEntry();
            outputEntry2.setName("SocketOutput");
            outputEntry2.setParameters(Map.of("socketSettings",
                    List.of(
                            new SocketEntry("localhost", 6000)
                    )
            ));
            eventManager.addOutput(outputEntry2);

            for (int i = 0; i < 10000; i++) {
                eventManager.logErrorMessage("This is an error message");
            }

            // Check if the console output contains the error message
            waitForEvents();
            assertTrue(outContent.toString().contains("This is an error message"));
            String received = receivedEvent.get(2, TimeUnit.SECONDS);
            assertTrue(received.contains("This is an error message"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Clean up: Reset System.out
            System.setOut(originalOut);
        }
    }

    @Test
    void removeOutputAndVerifyOutput() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LogHandler logHandler = new LogHandler(configPath, true);
        logHandler.getConfig().getEvent().setEventFormat("json");

        OutputEntry outputEntry = new OutputEntry();
        outputEntry.setName("PrintOutput");
        logHandler.getConfig().getOutputs().add(outputEntry);

        this.eventManager = new EventManager(logHandler);

        eventManager.removeOutput(new OutputEntry("PrintOutput", null));
        eventManager.logErrorMessage("This is an error message");

        // Check if the console output contains the error message
        waitForEvents();
        String output = outContent.toString();

        assertTrue(output.isEmpty());

        System.setOut(originalOut);
    }

    @Test
    void addProcessorAndVerifyOutput() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LogHandler logHandler = new LogHandler(configPath, true);
        logHandler.getConfig().getEvent().setEventFormat("json");

        OutputEntry outputEntry = new OutputEntry();
        outputEntry.setName("PrintOutput");
        logHandler.getConfig().getOutputs().add(outputEntry);

        this.eventManager = new EventManager(logHandler);

        ProcessorEntry processorEntry = new ProcessorEntry();
        processorEntry.setName("RegexProcessor");
        processorEntry.setParameters(Map.of("regexEntries",
                List.of(
                        new RegexEntry("level","ERROR", "KETCHUP")
                )
        ));

        eventManager.addProcessor(processorEntry);
        eventManager.logErrorMessage("This is an error message");

        // Check if the console output contains the error message
        waitForEvents();
        String output = outContent.toString();

        assertTrue(output.contains("\"level\":\"KETCHUP\""));

        System.setOut(originalOut);
    }

    @Test
    void removeProcessorAndVerifyOutput() {
        //Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LogHandler logHandler = new LogHandler(configPath, true);
        logHandler.getConfig().getEvent().setEventFormat("json");

        OutputEntry outputEntry = new OutputEntry();
        outputEntry.setName("PrintOutput");
        logHandler.getConfig().getOutputs().add(outputEntry);

        this.eventManager = new EventManager(logHandler);

        ProcessorEntry processorEntry = new ProcessorEntry();
        processorEntry.setName("RegexProcessor");
        processorEntry.setParameters(Map.of("regexEntries",
                List.of(
                        new RegexEntry("level","ERROR", "KETCHUP")
                )
        ));

        eventManager.addProcessor(processorEntry);
        eventManager.removeProcessor("RegexProcessor");
        eventManager.logErrorMessage("This is an error message");

        waitForEvents();

        // Check if the console output contains the error message
        String output = outContent.toString();

        assertTrue(output.contains("\"level\":\"ERROR\""));
        System.setOut(originalOut);

    }

    @Test
    void customLogLevel() {
        LogHandler logHandler = new LogHandler(configPath);
        logHandler.getConfig().getEvent().setEventFormat("default");
        logHandler.getConfig().getEvent().setPrintToConsole(false);
        EventManager eventManager = new EventManager(logHandler);
        eventManager.logCustomMessage("CUSTOM","This is a custom log level message");

        // Check if the log file exists
        assertTrue(logHandler.checkIfLogFileExists());

        // Check if the events were logged
        try {
            waitForEvents();
            String filePath = logHandler.getConfig().getLogFile().getFilePath();
            List<String> logLines = Files.readAllLines(Paths.get(filePath + logHandler.getCurrentFileName()));

            assertTrue(logLines.stream().anyMatch(line -> line.contains("This is a custom log level message")));
            assertTrue(logLines.stream().anyMatch(line -> line.contains("CUSTOM")));
            this.eventManager = eventManager;
        } catch (Exception e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }

    @Test
    void filterOutEvents() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LogHandler logHandler = new LogHandler(configPath, true);
        logHandler.getConfig().getEvent().setEventFormat("json");

        OutputEntry outputEntry = new OutputEntry();
        outputEntry.setName("PrintOutput");
        logHandler.getConfig().getOutputs().add(outputEntry);

        ProcessorEntry processorEntry = new ProcessorEntry();
        processorEntry.setName("FilterProcessor");
        processorEntry.setParameters(Map.of("termToFilter", List.of("test")));

        logHandler.getConfig().getProcessors().add(processorEntry);

        EventManager eventManager = new EventManager(logHandler);
        eventManager.logErrorMessage("This is a test message");
        eventManager.logErrorMessage("This is a message without the term");

        // Check if the console output contains the error message
        waitForEvents();
        String output = outContent.toString();
        assertTrue(output.contains("This is a message without the term"));
        assertFalse(output.contains("This is a test message"));

        this.eventManager = eventManager;
        System.setOut(originalOut);
    }

    @Test
    void sampleEvents() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LogHandler logHandler = new LogHandler(configPath, true);
        logHandler.getConfig().getEvent().setEventFormat("json");

        OutputEntry outputEntry = new OutputEntry();
        outputEntry.setName("PrintOutput");
        logHandler.getConfig().getOutputs().add(outputEntry);

        ProcessorEntry processorEntry = new ProcessorEntry();
        processorEntry.setName("SampleProcessor");
        processorEntry.setParameters(Map.of("sampleSize", 2));

        logHandler.getConfig().getProcessors().add(processorEntry);

        EventManager eventManager = new EventManager(logHandler);
        for (int i = 0; i < 10; i++) {
            eventManager.logErrorMessage("This is a test message " + i);
        }

        // Check if the console output contains the error message
        waitForEvents();
        String output = outContent.toString();
        assertTrue(output.contains("This is a test message 1"));
        assertTrue(output.contains("This is a test message 3"));
        assertTrue(output.contains("This is a test message 5"));
        assertTrue(output.contains("This is a test message 7"));
        assertTrue(output.contains("This is a test message 9"));

        this.eventManager = eventManager;
        System.setOut(originalOut);
    }
}