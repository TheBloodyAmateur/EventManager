package com.github.eventmanager.outputs;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.internal.InternalEventLogger;

public class PrintOutput implements Output {
    @Override
    public void write(LogHandler loghandler, String event) {
        System.out.println(event);
    }

    @Override
    public void write(InternalEventLogger internalEventLogger, String event) {
        System.out.println(event);
    }
}
