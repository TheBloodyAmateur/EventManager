package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ProcessorEntry {
    private String name;
    private Map<String, Object> parameters;

}