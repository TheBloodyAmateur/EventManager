package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegexEntry {
    String fieldName;
    String regex;
    String replacement;
}
