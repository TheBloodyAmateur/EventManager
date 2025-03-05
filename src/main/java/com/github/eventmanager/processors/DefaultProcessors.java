package com.github.eventmanager.processors;

import com.github.eventmanager.filehandlers.config.ProcessorEntry;

import java.util.List;

abstract public class DefaultProcessors {
    public static List<ProcessorEntry> createDefault() {
        ProcessorEntry entry = new ProcessorEntry();
        entry.setName("MaskPasswords");
        entry.setParameters(null);
        return List.of(entry);
    }
}