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

    private static final String EVENT_THREAD_NAME = "event-manager-event-thread";
    private static final String PROCESSING_THREAD_NAME = "event-manager-processing-thread";
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 5;

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
     * The thread is named for better debugging and monitoring.
     *
     * @param runnable the task to run in the event thread, typically involving log-writing operations.
     */
    public void startEventThread(Runnable runnable) {
        eventThread = new Thread(runnable, EVENT_THREAD_NAME);
        eventThread.setDaemon(false);
        eventThread.start();
    }

    /**
     * Starts and manages the event processing thread, executing the provided runnable task.
     * The thread is named for better debugging and monitoring.
     *
     * @param runnable the task to run in the processing thread, typically involving pre-processing or modifying event data.
     */
    public void startProcessingThread(Runnable runnable) {
        processingThread = new Thread(runnable, PROCESSING_THREAD_NAME);
        processingThread.setDaemon(false);
        processingThread.start();
    }

    /**
     * Gracefully interrupts and stops the specified thread, ensuring all remaining queued events
     * are processed using the provided {@link Consumer}.
     * <p>
     * This method also waits for the thread to finish with a timeout to ensure proper cleanup.
     *
     * @param thread the {@link Thread} instance to stop.
     * @param queue  the {@link BlockingQueue} holding the remaining events to be processed.
     * @param remainingItemProcessor a {@link Consumer} to process remaining events from the queue after interruption.
     */
    public void stopThread(Thread thread, BlockingQueue<String> queue, Consumer<String> remainingItemProcessor) {
        if (thread != null) {
            thread.interrupt();
            // Process remaining items in the queue
            while (!queue.isEmpty()) {
                String event = queue.poll();
                if (event != null) {
                    remainingItemProcessor.accept(event);
                }
            }
            // Wait for thread to finish with timeout
            try {
                thread.join(SHUTDOWN_TIMEOUT_SECONDS * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
