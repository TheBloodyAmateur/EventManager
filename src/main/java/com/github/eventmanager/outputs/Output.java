package com.github.eventmanager.outputs;

import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.filehandlers.LogHandler;

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
     * @param internalEventManager the InternalEventManager to use for writing the event.
     * @param event the event to write.
     */
    void write(InternalEventManager internalEventManager, String event);
}
