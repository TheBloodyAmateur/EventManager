package dev.github.eventmanager.formatters;

import java.util.Arrays;
import java.util.Map;

public enum EventFormatter {
    KEY_VALUE {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return "";
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return "";
        }
    },
    DEFAULT {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return String.format("[%s] %s %s %s %s: %s\n",
                    metadata.get("time"),
                    metadata.get("level"),
                    metadata.get("callerClassName"),
                    metadata.get("callerMethodName"),
                    metadata.get("lineNumber"),
                    Arrays.toString(args));
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return String.format("[%s] %s %s %s %s: %s\n",
                    metadata.get("time"),
                    metadata.get("level"),
                    metadata.get("callerClassName"),
                    metadata.get("callerMethodName"),
                    metadata.get("lineNumber"),
                    message);
        }
    };

    public abstract String format(Map<String,String> metadata, KeyValueWrapper ...args);
    public abstract String format(Map<String,String> metadata, String message);
}
