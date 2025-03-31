package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The OutputEntry class holds the configuration settings for the event outputs.
 * It includes settings for the name of the output and its parameters.
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