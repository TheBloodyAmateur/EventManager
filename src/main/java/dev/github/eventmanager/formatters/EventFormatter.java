package dev.github.eventmanager.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum EventFormatter {
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
                    metadata.get("className"),
                    metadata.get("methodName"),
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
    },
    KEY_VALUE {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
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
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                builder.append(entry.getKey()).append("=").append("\"").append(entry.getValue()).append("\"");
                builder.append(" ");
            }
            builder.append("message").append("=").append(message);

            builder.append("\n");

            return builder.toString();
        }
    },
    CSV {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                builder.append(entry.getValue());
                builder.append(",");
            }
            for (int i = 0; i < args.length; i++) {
                builder.append(args[i].getValue());
                if (i < args.length - 1) {
                    builder.append(",");
                }
            }

            builder.append("\n");

            return builder.toString();
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            StringBuilder builder = new StringBuilder();

            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                builder.append(entry.getValue());
                //Check if the entry is the last one
                if (!entry.equals(metadata.entrySet().toArray()[metadata.size() - 1])) {
                    builder.append(",");
                }
            }

            builder.append(message);

            builder.append("\n");

            return builder.toString();
        }
    },
    XML {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            StringBuilder builder = stringBuilderWithMetaData(metadata);

            for (KeyValueWrapper arg : args) {
                builder.append("<").append(arg.getKey()).append(">").append(arg.getValue()).append("</").append(arg.getKey()).append(">");
            }

            builder.append("</event>\n");

            return builder.toString();
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            StringBuilder builder = stringBuilderWithMetaData(metadata);

            builder.append("<").append("message").append(">").append(message).append("</").append("message").append(">");

            builder.append("</event>\n");

            return builder.toString();
        }

        private static StringBuilder stringBuilderWithMetaData(Map<String, String> metadata) {
            StringBuilder builder = new StringBuilder();
            builder.append("<event>");

            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                builder.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
            }

            return builder;
        }
    },
    JSON {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            Map<String, Object> event = new HashMap<>(metadata);
            for (KeyValueWrapper arg : args) {
                event.put(arg.getKey(), arg.getValue());
            }
            return convertToJson(event);
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            Map<String, Object> event = new HashMap<>(metadata);
            event.put("message", message);
            return convertToJson(event);
        }

        private String convertToJson(Map<String, Object> event) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(event) + "\n";
            } catch (Exception e) {
                return "{\"error\": \"Failed to convert to JSON\"}\n";
            }
        }
    };

    public abstract String format(Map<String, String> metadata, KeyValueWrapper... args);

    public abstract String format(Map<String, String> metadata, String message);
}
