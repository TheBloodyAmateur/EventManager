package com.github.eventmanager.outputs;

import com.github.eventmanager.filehandlers.config.OutputEntry;

import java.util.List;

public class DefaultOutput {
    /**
     * Creates a list of default output.
     *
     * @return a list of default output.
     */
    public static List<OutputEntry> createDefault() {
        OutputEntry entry = new OutputEntry();
        entry.setName("LogOutput");
        entry.setParameters(null);
        return List.of(entry);
    }
}
