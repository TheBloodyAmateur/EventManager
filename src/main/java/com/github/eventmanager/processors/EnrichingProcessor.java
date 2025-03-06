package com.github.eventmanager.processors;

import java.net.InetAddress;
import java.util.List;

/**
 * The EnrichingProcessor class is responsible for enriching event data with additional metadata.
 *
 * <p>It enriches event data with the information such as hostname, IP address, thread name, and thread ID.
 * */
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

    /**
     * Adds the specified field to the list of fields to enrich.
     *
     * @param field the field to enrich.
     * */
    private String getValue(String field) {
        return switch (field) {
            case "hostname" -> getHostname();
            case "ip" -> getIpAddress();
            case "osName" -> getOsName();
            case "osVersion" -> getOsVersion();
            case "javaVersion" -> getJavaVersion();
            case "userName" -> getUserName();
            case "availableProcessors" -> getAvailableProcessors();
            case "freeMemory" -> getFreeMemory();
            case "totalMemory" -> getTotalMemory();
            case "maxMemory" -> getMaxMemory();
            default -> "N\\A";
        };
    }

    /**
     * Enriches the specified event with additional metadata in key-value format.
     *
     * @param event to enrich.
     * */
    private String enrichKVEvent(String event) {
        StringBuilder builder = new StringBuilder(event);
        for (String field : enrichingFields) {
            builder.append(field).append("=\"").append(getValue(field)).append("\" ");
        }
        return builder.toString();
    }

    /**
     * Enriches the specified event with additional metadata in JSON format.
     *
     * @param event to enrich.
     * */
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

    /**
     * Enriches the specified event with additional metadata in XML format.
     *
     * @param event to enrich.
     * */
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

    /**
     * Returns the hostname of the machine.
     * */
    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * Returns the IP address of the machine.
     * */
    private String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * Returns the operating system name.
     * */
    private String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * Returns the operating system version.
     * */
    private String getOsVersion() {
        return System.getProperty("os.version");
    }

    /**
     * Returns the Java version.
     * */
    private String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Returns the username of the user running the application.
     * */
    private String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Returns the number of available processors.
     * */
    private String getAvailableProcessors() {
        return String.valueOf(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Returns the amount of free memory in the JVM.
     * */
    private String getFreeMemory() {
        return String.valueOf(Runtime.getRuntime().freeMemory());
    }

    /**
     * Returns the total memory in the JVM.
     * */
    private String getTotalMemory() {
        return String.valueOf(Runtime.getRuntime().totalMemory());
    }

    /**
     * Returns the maximum memory that the JVM can use.
     * */
    private String getMaxMemory() {
        return String.valueOf(Runtime.getRuntime().maxMemory());
    }
}
