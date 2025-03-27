package com.github.eventmanager.outputs;

import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.OutputEntry;
import com.github.eventmanager.filehandlers.config.SocketEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SocketOutputTest {
    String configPath = "config/loggingConfig.json";
    private EventManager eventManager;

    // Wait for events to be logged because the event thread is asynchronous
    public static void waitForEvents() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        eventManager.stopPipeline();
    }

    @Test
    void writeToSocket(){
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            int port = 6000;

            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Start mock socket server
            Future<String> receivedEvent = executor.submit(() -> {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    return reader.readLine(); // read a single event line
                }
            });

            // Give the server a moment
            waitForEvents();

            LogHandler logHandler = new LogHandler("configPath");
            OutputEntry outputEntry = new OutputEntry();
            outputEntry.setName("SocketOutput");
            outputEntry.setParameters(Map.of("socketSettings",
                    List.of(
                            new SocketEntry("localhost", port)
                    )
            ));
            logHandler.getConfig().getOutputs().add(outputEntry);

            eventManager = new EventManager(logHandler);
            for (int i = 0; i < 10000; i++) {
                eventManager.logErrorMessage("Hello from test!");
            }

            waitForEvents();

            String received = receivedEvent.get(2, TimeUnit.SECONDS);
            assertTrue(received.contains("Hello from test!"));

            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }
}