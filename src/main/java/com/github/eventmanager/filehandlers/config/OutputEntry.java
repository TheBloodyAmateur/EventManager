package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The OutputEntry class holds the configuration settings for the event outputs.
 * It includes settings for the name of the output and its parameters.
 *
 * This example demonstrates how to create an OutputEntry object to print output:
 *
 * <pre>{@code
 *  OutputEntry outputEntry = new OutputEntry();
 *  outputEntry.setName("PrintOutput");
 *  outputEntry.setParameters();
 *
 *  LogHandler logHandler = new LogHandler();
 *  logHandler.setOutput(outputEntry);
 * }</pre>
 *
 * This class is used to configure a socket output for the events.
 *
 * <pre>{@code
 *  OutputEntry socketOutput = new OutputEntry();
 *  socketOutput.setName("SocketOutput");
 *  socketOutput.setParameters(Map.of("socketSettings",
 *       List.of(
 *                new SocketEntry("localhost", 6000)
 *        )
 *  ));
 *
 *  EventManager eventManager = new EventManager();
 *  eventManager.setOutput(socketOutput);
 * }</pre>
 */
@Setter
@Getter
public class OutputEntry {
    /**
     * The name of the output(class).
     */
    private String name;
    /**
     * The parameters for the output.
     */
    private Map<String, Object> parameters;

    /**
     * Constructor to initialize the OutputEntry with a name and parameters.
     *
     * @param name       The name of the output.
     * @param parameters The parameters for the output.
     */
    public OutputEntry(String name, Map<String, Object> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public OutputEntry() {

    }
}