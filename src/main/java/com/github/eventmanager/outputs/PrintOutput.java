package com.github.eventmanager.outputs;

import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.filehandlers.LogHandler;

public class PrintOutput implements Output {
    @Override
    public void write(LogHandler loghandler, String event) {
        System.out.println(event);
    }

    @Override
    public void write(InternalEventManager internalEventManager, String event) {
        System.out.println(event);
    }
}
