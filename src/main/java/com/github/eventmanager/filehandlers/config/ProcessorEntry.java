package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The ProcessorEntry class holds the configuration settings for the event processors.
 * It includes settings for the name of the processor and its parameters.
 */
@Setter
@Getter
public class ProcessorEntry {
    /**
     * The name of the processor(class).
     */
    private String name;
    /**
     * The parameters for the processor.
     */
    private Map<String, Object> parameters;

}