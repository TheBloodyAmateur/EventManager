package com.github.eventmanager.outputs;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.internal.InternalEventLogger;

/**
 * The Output interface is used to define the methods that must be implemented by all Output classes.
 */
public interface Output {
    /**
     * Writes the given event to the internal log file.
     *
     * @param loghandler the LogHandler to use for writing the event.
     * @param event the event to write.
     */
    void write(LogHandler loghandler, String event);
    /**
     * Writes the given event to the log file.
     *
     * @param internalEventLogger the InternalEventLogger to use for writing the event.
     * @param event the event to write.
     */
    void write(InternalEventLogger internalEventLogger, String event);
}
