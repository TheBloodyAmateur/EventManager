package com.github.eventmanager.filehandlers.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Validates configuration values to ensure they are within acceptable ranges
 * and meet the requirements of the EventManager application.
 */
public final class ConfigValidator {
    
    private static final int MIN_MAX_SIZE_KB = 1;
    private static final int MAX_MAX_SIZE_KB = Integer.MAX_VALUE;
    private static final int MIN_ROTATION_PERIOD = 1;
    private static final int MAX_ROTATION_PERIOD = Integer.MAX_VALUE;
    
    private ConfigValidator() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Validates the entire configuration.
     *
     * @param config the configuration to validate.
     * @throws IllegalArgumentException if any configuration value is invalid.
     */
    public static void validateConfig(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        
        validateEventConfig(config.getEvent());
        validateLogFileConfig(config.getLogFile());
        validateLogRotateConfig(config.getLogRotateConfig());
        validateInternalEventsConfig(config.getInternalEvents());
        validateProcessors(config.getProcessors());
        validateOutputs(config.getOutputs());
    }
    
    /**
     * Validates event configuration.
     *
     * @param eventConfig the event configuration to validate.
     * @throws IllegalArgumentException if any value is invalid.
     */
    public static void validateEventConfig(ConfigEvent eventConfig) {
        if (eventConfig == null) {
            throw new IllegalArgumentException("Event configuration cannot be null");
        }
        
        String timeFormat = eventConfig.getTimeFormat();
        if (timeFormat == null || timeFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Time format cannot be null or empty");
        }
        
        String eventFormat = eventConfig.getEventFormat();
        if (eventFormat == null || eventFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Event format cannot be null or empty");
        }
        
        // Validate that the event format is one of the supported formats
        if (!isValidEventFormat(eventFormat)) {
            throw new IllegalArgumentException(
                "Invalid event format: " + eventFormat + ". Supported formats are: default, kv, csv, xml, json");
        }
    }
    
    /**
     * Validates log file configuration.
     *
     * @param logFileConfig the log file configuration to validate.
     * @throws IllegalArgumentException if any value is invalid.
     */
    public static void validateLogFileConfig(ConfigLogFile logFileConfig) {
        if (logFileConfig == null) {
            throw new IllegalArgumentException("Log file configuration cannot be null");
        }
        
        String filePath = logFileConfig.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        String fileName = logFileConfig.getFileName();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        
        String fileExtension = logFileConfig.getFileExtension();
        if (fileExtension == null || fileExtension.trim().isEmpty()) {
            throw new IllegalArgumentException("File extension cannot be null or empty");
        }
    }
    
    /**
     * Validates log rotation configuration.
     *
     * @param logRotateConfig the log rotation configuration to validate.
     * @throws IllegalArgumentException if any value is invalid.
     */
    public static void validateLogRotateConfig(ConfigLogRotate logRotateConfig) {
        if (logRotateConfig == null) {
            throw new IllegalArgumentException("Log rotation configuration cannot be null");
        }
        
        int maxSizeInKB = logRotateConfig.getMaxSizeInKB();
        if (maxSizeInKB < MIN_MAX_SIZE_KB) {
            throw new IllegalArgumentException(
                "Max size in KB must be at least " + MIN_MAX_SIZE_KB + ", got: " + maxSizeInKB);
        }
        
        int rotationPeriod = logRotateConfig.getRotationPeriodInSeconds();
        if (rotationPeriod < MIN_ROTATION_PERIOD) {
            throw new IllegalArgumentException(
                "Rotation period in seconds must be at least " + MIN_ROTATION_PERIOD + ", got: " + rotationPeriod);
        }
        
        String compressionFormat = logRotateConfig.getCompressionFormat();
        if (compressionFormat == null || compressionFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Compression format cannot be null or empty");
        }
        
        if (!isValidCompressionFormat(compressionFormat)) {
            throw new IllegalArgumentException(
                "Invalid compression format: " + compressionFormat + ". Supported formats are: gzip, zip");
        }
    }
    
    /**
     * Validates internal events configuration.
     *
     * @param internalEventsConfig the internal events configuration to validate.
     * @throws IllegalArgumentException if any value is invalid.
     */
    public static void validateInternalEventsConfig(ConfigInternalEvents internalEventsConfig) {
        if (internalEventsConfig == null) {
            throw new IllegalArgumentException("Internal events configuration cannot be null");
        }
        
        String filePath = internalEventsConfig.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Internal events file path cannot be null or empty");
        }
        
        String fileName = internalEventsConfig.getFileName();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Internal events file name cannot be null or empty");
        }
        
        String fileExtension = internalEventsConfig.getFileExtension();
        if (fileExtension == null || fileExtension.trim().isEmpty()) {
            throw new IllegalArgumentException("Internal events file extension cannot be null or empty");
        }
    }
    
    /**
     * Validates processor configurations.
     *
     * @param processors the list of processor configurations to validate.
     * @throws IllegalArgumentException if any processor configuration is invalid.
     */
    public static void validateProcessors(List<ProcessorEntry> processors) {
        if (processors == null) {
            throw new IllegalArgumentException("Processors list cannot be null");
        }
        
        for (ProcessorEntry processor : processors) {
            if (processor == null) {
                throw new IllegalArgumentException("Processor entry cannot be null");
            }
            
            String name = processor.getName();
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Processor name cannot be null or empty");
            }
        }
    }
    
    /**
     * Validates output configurations.
     *
     * @param outputs the list of output configurations to validate.
     * @throws IllegalArgumentException if any output configuration is invalid.
     */
    public static void validateOutputs(List<OutputEntry> outputs) {
        if (outputs == null) {
            throw new IllegalArgumentException("Outputs list cannot be null");
        }
        
        for (OutputEntry output : outputs) {
            if (output == null) {
                throw new IllegalArgumentException("Output entry cannot be null");
            }
            
            String name = output.getName();
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Output name cannot be null or empty");
            }
        }
    }
    
    /**
     * Checks if the event format is valid.
     *
     * @param format the event format to check.
     * @return true if the format is valid, false otherwise.
     */
    private static boolean isValidEventFormat(String format) {
        return format.equals("default") || 
               format.equals("kv") || 
               format.equals("csv") || 
               format.equals("xml") || 
               format.equals("json");
    }
    
    /**
     * Checks if the compression format is valid.
     *
     * @param format the compression format to check.
     * @return true if the format is valid, false otherwise.
     */
    private static boolean isValidCompressionFormat(String format) {
        return format.equals("gzip") || format.equals("zip");
    }
    
    /**
     * Checks if a file path exists and is writable.
     *
     * @param filePath the file path to check.
     * @return true if the path exists and is writable, false otherwise.
     */
    public static boolean isValidFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.isDirectory() && file.canWrite();
            }
            // If the path doesn't exist, check if we can create it
            return Files.isWritable(Paths.get(filePath).getParent());
        } catch (Exception e) {
            return false;
        }
    }
}
