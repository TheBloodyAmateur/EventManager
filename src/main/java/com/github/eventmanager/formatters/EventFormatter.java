package com.github.eventmanager.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * The EventFormatter enum provides different formatting strategies for event logs.
 * Each formatter formats the event metadata and arguments in a specific way.
 */
public enum EventFormatter {
    /**
     * The DEFAULT formatter formats the event metadata and arguments in a default format.
     */
    DEFAULT {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return formatDefault(metadata, args);
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return formatDefault(metadata, message);
        }

        @Override
        public String formatElement(KeyValueWrapper arg) {
            return arg.toString();
        }

        @Override
        public String formatArguments(String body, KeyValueWrapper... args) {
            return formatArgs(args);
        }
    },
    /**
     * The KEY_VALUE formatter formats the event metadata and arguments in a key-value format.
     */
    KEY_VALUE {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return formatKeyValue(metadata, args);
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return formatKeyValue(metadata, message);
        }

        @Override
        public String formatElement(KeyValueWrapper arg) {
            return arg.toString();
        }

        @Override
        public String formatArguments(String body, KeyValueWrapper... args) {
            return formatArgs(args);
        }
    },
    /**
     * The CSV formatter formats the event metadata and arguments in a CSV format.
     */
    CSV {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return formatCsv(metadata, args);
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return formatCsv(metadata, message);
        }

        @Override
        public String formatElement(KeyValueWrapper arg) {
            return arg.getValue();
        }

        @Override
        public String formatArguments(String body, KeyValueWrapper... args) {
            return formatCsvArgs(args);
        }
    },
    /**
     * The XML formatter formats the event metadata and arguments in an XML format.
     */
    XML {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return formatXml(metadata, args);
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return formatXml(metadata, message);
        }

        @Override
        public String formatElement(KeyValueWrapper arg) {
            return formatXmlElement(arg);
        }

        @Override
        public String formatArguments(String body, KeyValueWrapper... args) {
            return formatXmlArgs(body, args);
        }
    },
    /**
     * The JSON formatter formats the event metadata and arguments in a JSON format.
     */
    JSON {
        @Override
        public String format(Map<String, String> metadata, KeyValueWrapper... args) {
            return formatJson(metadata, args);
        }

        @Override
        public String format(Map<String, String> metadata, String message) {
            return formatJson(metadata, message);
        }

        @Override
        public String formatElement(KeyValueWrapper arg) {
            return formatJsonElement(arg);
        }

        @Override
        public String formatArguments(String body, KeyValueWrapper... args) {
            return formatJsonArgs(args);
        }
    };

    /**
     * Formats the event metadata and arguments.
     *
     * @param metadata the event metadata.
     * @param args the event arguments.
     * @return the formatted event as a string.
     */
    public abstract String format(Map<String, String> metadata, KeyValueWrapper... args);

    /**
     * Formats the event metadata and message.
     *
     * @param metadata the event metadata.
     * @param message the event message.
     * @return the formatted event as a string.
     */
    public abstract String format(Map<String, String> metadata, String message);

    /**
     * Formats the event element.
     *
     * @param arg the event element.
     * @return the formatted event element as a string.
     */
    public abstract String formatElement(KeyValueWrapper arg);

    /**
     * Formats the event arguments.
     *
     * @param body the argument keyword.
     * @param args the event arguments.
     * @return the formatted event arguments as a string.
     */
    public abstract String formatArguments(String body, KeyValueWrapper... args);

    /**
     * Formats the event metadata and arguments in a default format.
     * @param metadata the event metadata.
     * @param args the event arguments.
     * @return the formatted event as a string.
     * */
    private static String formatDefault(Map<String, String> metadata, KeyValueWrapper... args) {
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

    /**
     * Formats the event metadata and message in a default format.
     * @param metadata the event metadata.
     * @param message the event message.
     * @return the formatted event as a string.
     * */
    private static String formatDefault(Map<String, String> metadata, String message) {
        return String.format("[%s] %s %s %s %s: %s\n",
                metadata.get("time"),
                metadata.get("level"),
                metadata.get("className"),
                metadata.get("methodName"),
                metadata.get("lineNumber"),
                message);
    }

    /**
     * Formats the event metadata and arguments in a key-value format.
     * @param metadata the event metadata.
     * @param args the event arguments.
     * @return the formatted event as a string.
     * */
    private static String formatKeyValue(Map<String, String> metadata, KeyValueWrapper... args) {
        StringBuilder builder = new StringBuilder();
        appendMetadata(builder, metadata);
        for (KeyValueWrapper arg : args) {
            builder.append(arg.toString()).append(" ");
        }
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Formats the event metadata and message in a key-value format.
     * @param metadata the event metadata.
     * @param message the event message.
     * @return the formatted event as a string.
     * */
    private static String formatKeyValue(Map<String, String> metadata, String message) {
        StringBuilder builder = new StringBuilder();
        appendMetadata(builder, metadata);
        builder.append("message=").append(message).append("\n");
        return builder.toString();
    }

    /**
     * Formats the event metadata and arguments in a CSV format.
     * @param metadata the event metadata.
     * @param args the event arguments.
     * @return the formatted event as a string.
     * */
    private static String formatCsv(Map<String, String> metadata, KeyValueWrapper... args) {
        StringBuilder builder = new StringBuilder();
        appendCsvMetadata(builder, metadata);
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i].getValue());
            if (i < args.length - 1) {
                builder.append(",");
            }
        }
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Formats the event metadata and message in a CSV format.
     * @param args the event arguments.
     * */
    private static String formatCsvArgs(KeyValueWrapper... args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i].getValue());
            if (i < args.length - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    /**
     * Formats the event metadata and arguments in an XML format.
     * @param metadata the event metadata.
     * @param message the event message.
     * @return the formatted event as a string.
     * */
    private static String formatCsv(Map<String, String> metadata, String message) {
        StringBuilder builder = new StringBuilder();
        appendCsvMetadata(builder, metadata);
        builder.append(message).append("\n");
        return builder.toString();
    }

    /**
     * Formats the event metadata and arguments in an XML format.
     * @param metadata the event metadata.
     * @param args the event arguments.
     * @return the formatted event as a string.
     * */
    private static String formatXml(Map<String, String> metadata, KeyValueWrapper... args) {
        StringBuilder builder = stringBuilderWithMetaData(metadata);
        for (KeyValueWrapper arg : args) {
            builder.append("<").append(arg.getKey()).append(">").append(arg.getValue()).append("</").append(arg.getKey()).append(">");
        }
        builder.append("</event>\n");
        return builder.toString();
    }

    /**
     * Formats the event metadata and message in an XML format.
     * @param metadata the event metadata.
     * @param message the event message.
     * @return the formatted event as a string.
     * */
    private static String formatXml(Map<String, String> metadata, String message) {
        StringBuilder builder = stringBuilderWithMetaData(metadata);
        builder.append("<message>").append(message).append("</message>");
        builder.append("</event>\n");
        return builder.toString();
    }

    /**
     * Formats the event metadata and arguments in a JSON format.
     * @param metadata the event metadata.
     * @param args the event arguments.
     * @return the formatted event as a string.
     * */
    private static String formatJson(Map<String, String> metadata, KeyValueWrapper... args) {
        Map<String, Object> event = new HashMap<>(metadata);
        for (KeyValueWrapper arg : args) {
            event.put(arg.getKey(), arg.getValue());
        }
        return convertToJson(event);
    }

    /**
     * Formats the event metadata and message in a JSON format.
     * @param metadata the event metadata.
     * @param message the event message.
     * @return the formatted event as a string.
     * */
    private static String formatJson(Map<String, String> metadata, String message) {
        Map<String, Object> event = new HashMap<>(metadata);
        event.put("message", message);
        return convertToJson(event);
    }

    /**
     * Converts the event to a JSON string.
     * @param event the event to convert.
     * @return the event as a JSON string.
     * */
    private static String convertToJson(Map<String, Object> event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(event) + "\n";
        } catch (Exception e) {
            return "{\"error\": \"Failed to convert to JSON\"}\n";
        }
    }

    /**
     * Appends the metadata to the event log.
     * @param builder the event log builder.
     * @param metadata the metadata to append.
     * */
    private static void appendMetadata(StringBuilder builder, Map<String, String> metadata) {
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            builder.append(entry.getKey()).append("=").append("\"").append(entry.getValue()).append("\" ").append(" ");
        }
    }

    /**
     * Appends the metadata to the CSV event log.
     * @param builder the event log builder.
     * @param metadata the metadata to append.
     * */
    private static void appendCsvMetadata(StringBuilder builder, Map<String, String> metadata) {
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            builder.append(entry.getValue()).append(",");
        }
    }

    /**
     * Creates a StringBuilder with the metadata.
     * @param metadata the metadata to append.
     * @return the StringBuilder with the metadata.
     * */
    private static StringBuilder stringBuilderWithMetaData(Map<String, String> metadata) {
        StringBuilder builder = new StringBuilder();
        builder.append("<event>");
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            builder.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        return builder;
    }

    /**
     * Creates a formatted XML element.
     * @param arg the event argument.
     * */
    private static String formatXmlElement(KeyValueWrapper arg) {
        return "<" + arg.getKey() + ">" + arg.getValue() + "</" + arg.getKey() + ">";
    }

    /**
     * Formats the event arguments in an XML format.
     * @param body the argument keyword.
     * @param args the event arguments.
     * @return the formatted event arguments as a string.
     * */
    private static String formatXmlArgs(String body, KeyValueWrapper... args) {
        StringBuilder builder = new StringBuilder();
        builder.append("<").append(body).append(">");
        for (KeyValueWrapper arg : args) {
            builder.append("<").append(arg.getKey()).append(">").append(arg.getValue()).append("</").append(arg.getKey()).append(">");
        }
        builder.append("</").append(body).append(">");
        return builder.toString();
    }

    /**
     * Formats the event arguments in a JSON format.
     * @param arg the event argument.
     * */
    private static String formatJsonElement(KeyValueWrapper arg) {
        return "\"" + arg.getKey() + "\": \"" + arg.getValue() + "\"";
    }

    /**
     * Formats the event arguments in a JSON format.
     * @param args the event arguments.
     * @return the formatted event arguments as a string.
     * */
    private static String formatJsonArgs(KeyValueWrapper... args) {
        Map<String, Object> event = new HashMap<>();
        for (KeyValueWrapper arg : args) {
            event.put(arg.getKey(), arg.getValue());
        }
        return "\"args\": {" + convertToJson(event).substring(1, convertToJson(event).length() - 2) + "}";
    }

    /**
     * Formats the event arguments.
     * @param args the event arguments.
     * @return the formatted event arguments as a string.
     * */
    private static String formatArgs(KeyValueWrapper... args) {
        StringBuilder builder = new StringBuilder();
        for (KeyValueWrapper arg : args) {
            builder.append(arg.toString()).append(" ");
        }
        return builder.toString();
    }
}