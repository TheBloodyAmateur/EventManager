package com.github.eventmanager.processors;

import com.github.eventmanager.filehandlers.config.ProcessorEntry;

import java.util.List;

/**
 * The DefaultProcessors class provides a method to create a list of default processors.
 */
abstract public class DefaultProcessors {
    /**
     * Creates a list of default processors.
     *
     * @return a list of default processors.
     */
    public static List<ProcessorEntry> createDefault() {
        ProcessorEntry entry = new ProcessorEntry();
        entry.setName("MaskPasswords");
        entry.setParameters(null);
        return List.of(entry);
    }
}