package com.github.eventmanager.filehandlers.config;

    import lombok.Getter;

    /**
     * The Config class holds the configuration settings for the EventManager application.
     * It includes settings for events, log files, and log rotation.
     * <p>
     * This class aggregates the configuration settings into three main categories:
     * <ul>
     *   <li>{@link ConfigEvent} - Configuration settings related to events.</li>
     *   <li>{@link ConfigLogFile} - Configuration settings related to log files.</li>
     *   <li>{@link ConfigLogRotate} - Configuration settings related to log file rotation.</li>
     * </ul>
     */
    @Getter
    public class Config {
        /**
         * Configuration settings related to events.
         * <p>
         * This includes settings such as whether to print logs to the console,
         * whether to print and save logs to a file, debugging mode, informational mode,
         * time format for logging events, and event format for logging events.
         */
        private ConfigEvent event = new ConfigEvent();

        /**
         * Configuration settings related to log files.
         * <p>
         * This includes settings such as the file name, file extension, and file path
         * for the log files.
         */
        private ConfigLogFile logFile = new ConfigLogFile();

        /**
         * Configuration settings related to log file rotation.
         * <p>
         * This includes settings such as the rotation period in seconds, maximum size
         * in kilobytes for log files, and the compression format to use when rotating log files.
         */
        private ConfigLogRotate logRotateConfig = new ConfigLogRotate();
    }