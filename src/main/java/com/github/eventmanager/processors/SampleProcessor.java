package com.github.eventmanager.processors;

import java.util.concurrent.atomic.AtomicInteger;

public class SampleProcessor implements Processor {
    private final AtomicInteger sampleSize = new AtomicInteger();
    private final AtomicInteger sampleCount = new AtomicInteger();

    public SampleProcessor(int sampleSize) {
        // Constructor logic if needed
        this.sampleSize.set(sampleSize-1);
        this.sampleCount.set(0);
    }
    @Override
    public String processKV(String event) {
        return processEvent(event);
    }

    @Override
    public String processJSON(String event) {
        return processEvent(event);
    }

    @Override
    public String processXML(String event) {
        return processEvent(event);
    }

    private String processEvent(String event) {
        if (sampleCount.get() < sampleSize.get()) {
            sampleCount.incrementAndGet();
            // Return the event
            return "";
        } else {
            sampleCount.set(0);
            // Discard the event
            return event;
        }
    }
}
