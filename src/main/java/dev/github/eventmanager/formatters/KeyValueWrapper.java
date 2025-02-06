package dev.github.eventmanager.formatters;

import lombok.Getter;

public class KeyValueWrapper {
    private String key;
    @Getter
    private String value;

    public KeyValueWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=\"" + value + "\"";
    }
}
