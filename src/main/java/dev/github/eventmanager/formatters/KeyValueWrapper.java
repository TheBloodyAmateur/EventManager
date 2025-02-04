package dev.github.eventmanager.formatters;

public class KeyValueWrapper {
    private String key;
    private String value;

    public KeyValueWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
