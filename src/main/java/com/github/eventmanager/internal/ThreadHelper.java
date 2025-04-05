package com.github.eventmanager.internal;

import lombok.Getter;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * Helper class responsible for managing event-related threads within the EventManager library.
 * <p>
 * Provides functionality for:
 * <ul>
 *     <li>Starting and managing threads specifically for event processing and logging.</li>
 *     <li>Gracefully stopping threads, ensuring queued events are fully processed.</li>
 * </ul>
 */
@Getter
public class ThreadHelper {

    /**
     * Thread dedicated to handling event logging operations.
     */
    private Thread eventThread;

    /**
     * Thread dedicated to processing events prior to logging.
     */
    private Thread processingThread;

    /**
     * Starts and manages the event logging thread, executing the provided runnable task.
     *
     * @param runnable the task to run in the event thread, typically involving log-writing operations.
     */
    public void startEventThread(Runnable runnable) {
        eventThread = new Thread(runnable);
        eventThread.start();
    }

    /**
     * Starts and manages the event processing thread, executing the provided runnable task.
     *
     * @param runnable the task to run in the processing thread, typically involving pre-processing or modifying event data.
     */
    public void startProcessingThread(Runnable runnable) {
        processingThread = new Thread(runnable);
        processingThread.start();
    }

    /**
     * Gracefully interrupts and stops the specified thread, ensuring all remaining queued events
     * are processed using the provided {@link Consumer}.
     *
     * @param thread the {@link Thread} instance to stop.
     * @param queue  the {@link BlockingQueue} holding the remaining events to be processed.
     * @param remainingItemProcessor a {@link Consumer} to process remaining events from the queue after interruption.
     */
    public void stopThread(Thread thread, BlockingQueue<String> queue, Consumer<String> remainingItemProcessor) {
        thread.interrupt();
        while (!queue.isEmpty()) {
            String event = queue.poll();
            if (event != null) {
                remainingItemProcessor.accept(event);
            }
        }
    }
}
