package com.github.eventmanager.internal;

import com.github.eventmanager.filehandlers.LogHandler;

/**
 * Interface for logging internal events, used to break circular dependencies
 * between ManagerBase and InternalEventManager.
 */
public interface InternalEventLogger {
    
    /**
     * Get the LogHandler associated with this logger.
     * @return the LogHandler instance.
     */
    LogHandler getLogHandler();

    /**
     * Log a fatal internal event.
     * @param message the message to log.
     */
    void logFatal(String message);

    /**
     * Log an error internal event.
     * @param message the message to log.
     */
    void logError(String message);

    /**
     * Log a warning internal event.
     * @param message the message to log.
     */
    void logWarn(String message);

    /**
     * Log an informational internal event.
     * @param message the message to log.
     */
    void logInfo(String message);

    /**
     * Log a debug internal event.
     * @param message the message to log.
     */
    void logDebug(String message);

    /**
     * Check if info logs are enabled.
     * @return true if info logs are enabled.
     */
    boolean areInfoLogsEnabled();

    /**
     * Stop the internal event pipeline.
     */
    void stopPipeline();
}
