package com.github.eventmanager.outputs;

import lombok.Getter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a batch of strings that can be added to. Used to batch strings together before sending them to an output.
 */
public class Batch {
    /**
     * The maximum number of bytes that the batch can hold.
     */
    private final int maxBytes;
    /**
     * The list of strings in the batch.
     */
    @Getter
    private final List<String> batch = new ArrayList<>();
    private int currentBytes = 0;

    public Batch(int maxBytes) {
        this.maxBytes = maxBytes;
    }

    public boolean tryAdd(String s) {
        // Check if adding the string will exceed the maxBytes
        int byteLen = s.getBytes(Charset.defaultCharset()).length;
        if (currentBytes + byteLen > maxBytes) {
            return false;
        }
        // Add the string to the batch and update the currentBytes
        batch.add(s);
        currentBytes += byteLen;
        return true;
    }

    public void clearBatch() {
        batch.clear();
        currentBytes = 0;
    }

    public int getCurrentSizeInBytes() {
        return currentBytes;
    }
}
