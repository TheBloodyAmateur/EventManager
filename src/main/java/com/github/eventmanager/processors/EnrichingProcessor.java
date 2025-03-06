package com.github.eventmanager.processors;

import java.net.InetAddress;
import java.util.List;

public class EnrichingProcessor implements Processor {
    private List<String> enrichingFields = List.of("hostname", "ip");

    @Override
    public String processKV(String event) {
        return enrichKVEvent(event);
    }

    @Override
    public String processJSON(String event) {
        return enrichJSONEvent(event);
    }

    @Override
    public String processXML(String event) {
        return enrichXMLEvent(event);
    }

    private String getValue(String field) {
        return switch (field) {
            case "hostname" -> getHostname();
            case "ip" -> getIpAddress();
            case "threadName" -> getThreadName();
            case "threadID" -> getThreadID();
            default -> "N\\A";
        };
    }

    private String enrichKVEvent(String event) {
        StringBuilder builder = new StringBuilder(event);
        for (String field : enrichingFields) {
            builder.append(field).append("=\"").append(getValue(field)).append("\" ");
        }
        return builder.toString();
    }

    private String enrichJSONEvent(String event) {
        StringBuilder builder = new StringBuilder(event);
        // Remove the last character which is  a closing curly brace
        builder.deleteCharAt(builder.length() - 1).append(",");
        for (String field : enrichingFields) {
            builder.append("\"").append(field).append("\":\"").append(getValue(field)).append("\",");
        }
        builder.deleteCharAt(builder.length() - 1).append("}");

        return builder.toString();
    }

    private String enrichXMLEvent(String event) {
        StringBuilder builder = new StringBuilder(event);
        // Remove the last 8 characters which are </event>
        builder.delete(builder.length() - 8, builder.length());
        for (String field : enrichingFields) {
            builder.append("<").append(field).append(">").append(getValue(field)).append("</").append(field).append(">");
        }
        builder.append("</event>");
        return builder.toString();
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    private String getThreadID() {
        return String.valueOf(Thread.currentThread().getId());
    }
}
