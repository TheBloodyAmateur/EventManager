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

}