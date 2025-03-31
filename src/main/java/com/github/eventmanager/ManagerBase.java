package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.OutputEntry;
import com.github.eventmanager.filehandlers.config.ProcessorEntry;
import com.github.eventmanager.formatters.EventFormatter;
import com.github.eventmanager.formatters.KeyValueWrapper;
import com.github.eventmanager.helpers.EventMetaDataBuilder;
import com.github.eventmanager.helpers.OutputHelper;
import com.github.eventmanager.helpers.ProcessorHelper;
import com.github.eventmanager.helpers.ThreadHelper;
import com.github.eventmanager.outputs.Output;
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
     * Helper class for output operations.
     */
    protected OutputHelper outputHelper;

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
    private final ThreadHelper threadHelper = new ThreadHelper();

    /**
     * Initializes ManagerBase with a provided LogHandler instance.
     *
     * @param logHandler The LogHandler responsible for managing logging operations.
     */
    public ManagerBase(LogHandler logHandler) {
        this.logHandler = logHandler;
        this.processorHelper = new ProcessorHelper(logHandler);
        this.outputHelper = new OutputHelper(logHandler);
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
        initialiseProcessorThreadAndOutputs();

        threadHelper.startEventThread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String event = eventQueue.take();
                    outputEvent(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Starts event processing and logging threads.
     */
    protected void initiateThreads(InternalEventManager internalEventManager) {
        initialiseProcessorThreadAndOutputs();

        threadHelper.startEventThread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String event = eventQueue.take();
                    outputEvent(internalEventManager, event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Initializes the processing thread and output destinations.
     */
    private void initialiseProcessorThreadAndOutputs() {
        processorHelper.initialiseProcessors();
        outputHelper.initialiseOutputs();

        threadHelper.startProcessingThread(() -> {
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
    }

    /**
     * Stops all threads gracefully and processes remaining events,
     * using InternalEventManager for structured logging of shutdown status.
     *
     * @param internalEventManager The event manager used for logging shutdown information.
     */
    protected void stopAllThreads(InternalEventManager internalEventManager) {
        threadHelper.stopThread(threadHelper.getProcessingThread(), processingQueue, event -> {
            try {
                event = processorHelper.processEvent(event);
                writeEventToQueue(event);
            } catch (Exception e) {
                internalEventManager.logError("Error processing remaining events: " + e.getMessage());
            }
        });
        internalEventManager.logInfo("Processing queue processed successfully.");

        threadHelper.stopThread(threadHelper.getEventThread(), eventQueue, event -> {
            try {
                outputEvent(event);
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
        threadHelper.stopThread(threadHelper.getProcessingThread(), processingQueue, event -> {
            try {
                event = processorHelper.processEvent(event);
                writeEventToQueue(event);
            } catch (Exception e) {
                System.out.println("Error processing remaining events: " + e.getMessage());
            }
        });
        System.out.println("Processing queue processed successfully.");

        threadHelper.stopThread(threadHelper.getEventThread(), eventQueue, event -> {
            try {
                outputEvent(event);
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
     * Passes the event to the output or outputs specified in the runtime or config specification.
     */
    protected void outputEvent(String event){
        this.outputHelper.outputEvent(event);
    }

    /**
     * Passes the event to the output or outputs specified in the runtime or config specification.
     */
    protected void outputEvent(InternalEventManager internalEventManager, String event){
        this.outputHelper.outputEvent(internalEventManager, event);
    }

    /**
     * Adds a new output destination.
     *
     * @param output The output entry to add.
     * @return true if the output was added successfully, false otherwise.
     */
    protected boolean addOutput(OutputEntry output) {
        return this.outputHelper.addOutput(output);
    }

    /**
     * Removes an output destination.
     *
     * @param outputEntry The output entry to remove.
     * @return true if the output was removed successfully, false otherwise.
     */
    protected boolean removeOutput(OutputEntry outputEntry) {
        return this.outputHelper.removeOutput(outputEntry);
    }

    /**
     * Removes an output destination by class name.
     *
     * @param outputName The class name of the output to remove.
     * @return true if the output was removed successfully, false otherwise.
     */
    protected boolean removeOutput(String outputName) {
        return this.outputHelper.removeOutput(outputName);
    }

    /**
     * Adds a new processor to the list of processors.
     *
     * @param processorName The processor entry to add.
     * @return true if the processor was added successfully, false otherwise.
     */
    protected boolean addProcessor(ProcessorEntry processorName) {
        return this.processorHelper.addProcessor(processorName);
    }

    /**
     * Removes a processor from the list of processors.
     *
     * @param processorName The class name of the processor to remove.
     * @return true if the processor was removed successfully, false otherwise.
     */
    protected boolean removeProcessor(String processorName) {
        return this.processorHelper.removeProcessor(processorName);
    }

    /**
     * Removes a processor from the list of processors.
     *
     * @param processorEntry The processor entry to remove.
     * @return true if the processor was removed successfully, false otherwise.
     */
    protected boolean removeProcessor(ProcessorEntry processorEntry) {
        return this.processorHelper.removeProcessor(processorEntry);
    }
}
