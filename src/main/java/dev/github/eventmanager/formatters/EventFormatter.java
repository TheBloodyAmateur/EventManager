package dev.github.eventmanager.formatters;

import java.util.Arrays;
import java.util.Map;

public enum EventFormatter {
    KEY_VALUE {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper ...args) {
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String, String> entry : metadata.entrySet()) {
                builder.append(entry.getKey()).append("=").append("\"").append(entry.getValue()).append("\"");
                builder.append(" ");
            }
            for (KeyValueWrapper arg : args) {
                builder.append(arg.toString());
                builder.append(" ");
            }

            builder.append("\n");

            return builder.toString();
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String, String> entry : metadata.entrySet()) {
                builder.append(entry.getKey()).append("=").append("\"").append(entry.getValue()).append("\"");
                builder.append(" ");
            }
            builder.append("message").append("=").append(message);

            builder.append("\n");

            return builder.toString();
        }
    },
    DEFAULT {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            StringBuilder builder = new StringBuilder();

            for (KeyValueWrapper arg : args) {
                builder.append(arg.toString()).append(" ");
            }

            return String.format("[%s] %s %s %s %s: %s\n",
                    metadata.get("time"),
                    metadata.get("level"),
                    metadata.get("callerClassName"),
                    metadata.get("callerMethodName"),
                    metadata.get("lineNumber"),
                    builder);
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
