package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The ProcessorEntry class holds the configuration settings for the event processors.
 * It includes settings for the name of the processor and its parameters.
 *
 * This example demonstrates how to create a ProcessorEntry object to configure a processor:
 *
 * <pre>{@code
 *  ProcessorEntry processorEntry = new ProcessorEntry();
 *  processorEntry.setName("FilterProcessor");
 *  processorEntry.setParameters(Map.of("termToFilter", List.of("error", "warning")));
 *
 *  EventManager eventManager = new EventManager();
 *  eventManager.setProcessor(processorEntry);
 *  }</pre>
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