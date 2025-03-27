package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.formatters.EventFormatter;
import com.github.eventmanager.formatters.KeyValueWrapper;
import com.github.eventmanager.helpers.EventMetaDataBuilder;
import com.github.eventmanager.helpers.ProcessorHelper;
import com.github.eventmanager.helpers.ThreadHelper;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Abstract base class providing foundational functionality for event management,
 * including event queuing, processing, and logging.
 */
public abstract class ManagerBase {

    /**
     * Handles log file writing and log configuration.
     */
    @Getter
    protected LogHandler logHandler;

    /**
     * Processes and enriches log events.
     */
    protected ProcessorHelper processorHelper;

    /**
     * Queue that holds events ready to be written to the log file.
     */
    protected final BlockingQueue<String> eventQueue = new LinkedBlockingQueue<>();

    /**
     * Queue that holds events pending processing by processors.
     */
    protected final BlockingQueue<String> processingQueue = new LinkedBlockingQueue<>();

    /**
     * Manages threading operations for event and processing threads.
     */
    private final ThreadHelper threadManager = new ThreadHelper();

    /**
     * Initializes ManagerBase with a provided LogHandler instance.
     *
     * @param logHandler The LogHandler responsible for managing logging operations.
     */
    public ManagerBase(LogHandler logHandler) {
        this.logHandler = logHandler;
        this.processorHelper = new ProcessorHelper(logHandler);
    }

    /**
     * Initializes ManagerBase using a configuration file path.
     *
     * @param configPath Path to the logging configuration file.
     */
    public ManagerBase(String configPath) {
        this(new LogHandler(configPath));
    }

    /**
     * Starts event processing and logging threads.
     */
    protected void initiateThreads() {
        processorHelper.initialiseProcessors();

        threadManager.startProcessingThread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String event = processingQueue.take();
                    event = processorHelper.processEvent(event);
                    writeEventToQueue(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadManager.startEventThread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String event = eventQueue.take();
                    writeEventToLogFile(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Stops all threads gracefully and processes remaining events,
     * using InternalEventManager for structured logging of shutdown status.
     *
     * @param internalEventManager The event manager used for logging shutdown information.
     */
    protected void stopAllThreads(InternalEventManager internalEventManager) {
        threadManager.stopThread(threadManager.getProcessingThread(), processingQueue, event -> {
            try {
                event = processorHelper.processEvent(event);
                writeEventToQueue(event);
            } catch (Exception e) {
                internalEventManager.logError("Error processing remaining events: " + e.getMessage());
            }
        });
        internalEventManager.logInfo("Processing queue processed successfully.");

        threadManager.stopThread(threadManager.getEventThread(), eventQueue, event -> {
            try {
                writeEventToLogFile(event);
            } catch (Exception e) {
                internalEventManager.logError("Error writing remaining events: " + e.getMessage());
            }
        });
        internalEventManager.logInfo("Event queue processed successfully.");
    }

    /**
     * Stops all threads gracefully without structured logging.
     * Logs status information directly to the standard output.
     */
    protected void stopAllThreads() {
        threadManager.stopThread(threadManager.getProcessingThread(), processingQueue, event -> {
            try {
                event = processorHelper.processEvent(event);
                writeEventToQueue(event);
            } catch (Exception e) {
                System.out.println("Error processing remaining events: " + e.getMessage());
            }
        });
        System.out.println("Processing queue processed successfully.");

        threadManager.stopThread(threadManager.getEventThread(), eventQueue, event -> {
            try {
                writeEventToLogFile(event);
            } catch (Exception e) {
                System.out.println("Error writing remaining events: " + e.getMessage());
            }
        });
        System.out.println("Event queue processed successfully.");
    }

    /**
     * Formats and queues a log message for processing and eventual writing to log file.
     *
     * @param level   Log level (e.g., INFO, ERROR).
     * @param message Message content to log, which can be an Exception or String.
     */
    protected void logMessage(String level, Object message) {
        String formattedMessage = (message instanceof Exception)
                ? ((Exception) message).getMessage()
                : message.toString();

        Map<String, String> metaData = EventMetaDataBuilder.buildMetaData(level, this.logHandler);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        String event = switch (eventFormat) {
            case "kv" -> EventFormatter.KEY_VALUE.format(metaData, formattedMessage);
            case "csv" -> EventFormatter.CSV.format(metaData, formattedMessage);
            case "xml" -> EventFormatter.XML.format(metaData, formattedMessage);
            case "json" -> EventFormatter.JSON.format(metaData, formattedMessage);
            default -> EventFormatter.DEFAULT.format(metaData, formattedMessage);
        };

        writeEventToProcessingQueue(event);
    }

    /**
     * Logs a message to the destination file.
     *
     * @param level    the log level of the message.
     * @param messages an object array to be appended to the message.
     */
    protected void logMessage(String level, KeyValueWrapper... messages) {
        Map<String, String> metaData = EventMetaDataBuilder.buildMetaData(level, this.logHandler);
        String eventFormat = this.logHandler.getConfig().getEvent().getEventFormat();

        String event = switch (eventFormat) {
            case "kv" -> EventFormatter.KEY_VALUE.format(metaData, messages);
            case "csv" -> EventFormatter.CSV.format(metaData, messages);
            case "xml" -> EventFormatter.XML.format(metaData, messages);
            case "json" -> EventFormatter.JSON.format(metaData, messages);
            default -> EventFormatter.DEFAULT.format(metaData, messages);
        };;

        writeEventToProcessingQueue(event);
    }

    /**
     * Adds processed event to the event queue.
     *
     * @param event The event string after processing.
     */
    protected void writeEventToQueue(String event) {
        eventQueue.add(event);
    }

    /**
     * Adds raw event to the processing queue.
     *
     * @param event The event string before processing.
     */
    protected void writeEventToProcessingQueue(String event) {
        processingQueue.add(event);
    }

    /**
     * Abstract method for writing the event directly to the log file.
     *
     * @param event The formatted event string ready for logging.
     */
    protected abstract void writeEventToLogFile(String event);
}
