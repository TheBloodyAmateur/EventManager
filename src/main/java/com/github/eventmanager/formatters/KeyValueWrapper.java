package com.github.eventmanager.formatters;

import lombok.Getter;

/**
 * The KeyValueWrapper class is a simple data structure that holds a key-value pair.
 * It provides methods to retrieve the key and value, and to represent the key-value pair as a string.
 */
@Getter
public class KeyValueWrapper {
    private String key;
    private String value;

    /**
     * Constructs a KeyValueWrapper with the specified key and value.
     *
     * @param key   the key of the key-value pair.
     * @param value the value of the key-value pair.
     */
    public KeyValueWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns a string representation of the key-value pair in the format key="value".
     *
     * @return the string representation of the key-value pair.
     */
    @Override
    public String toString() {
        return key + "=\"" + value + "\"";
    }
}