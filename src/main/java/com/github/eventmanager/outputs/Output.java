package com.github.eventmanager.outputs;

import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.filehandlers.LogHandler;

public interface Output {
    void write(LogHandler loghandler, String event);
    void write(InternalEventManager internalEventManager, String event);
}
