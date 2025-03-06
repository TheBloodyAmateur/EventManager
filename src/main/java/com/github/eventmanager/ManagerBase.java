package com.github.eventmanager;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.ProcessorEntry;
import com.github.eventmanager.formatters.EventFormatter;
import com.github.eventmanager.processors.MaskIPV4Address;
import com.github.eventmanager.processors.Processor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The ManagerBase class is responsible for queuing and writing events to the log file.
 * It provides a thread-safe queue to store events and a thread to write events to the log file.
 * */
abstract class ManagerBase {
    @Getter
    protected LogHandler logHandler;
    protected Thread eventThread;
    protected Thread processingThread;
    @Getter
    @Setter
    protected List<Processor> processors = new ArrayList<>();
    protected final BlockingQueue<String> eventQueue = new LinkedBlockingQueue<>();
    protected final BlockingQueue<String> processingQueue = new LinkedBlockingQueue<>();

    /**
     * Constructs an ManagerBase with the specified LogHandler.
     *
     * @param logHandler the LogHandler to use for logging events.
     */
    public ManagerBase(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    /**
     * Constructs an ManagerBase with the specified configuration file path.
     *
     * @param configPath the path to the configuration file. Passing an empty string or invalid path will force
     *                   the ManagerBase to fall back to the default configuration.
     *
     */
    public ManagerBase(String configPath) {
        this.logHandler = new LogHandler(configPath);
    }

    protected void initiateThreads() {
        initialiseProcessors();
        this.processingThread = initiateProcessingThread();
        this.eventThread = initiateEventThread();
    }

    protected String processEvent(String event) {
        for (Processor processor : processors) {
            switch (this.logHandler.getConfig().getEvent().getEventFormat())
            {
                case "kv" -> event = processor.processKV(event);
                case "xml" -> event = processor.processXML(event);
                case "json" -> event = processor.processJSON(event);
            }
        }
        return event;
    }

    private void initialiseProcessors(){
        for (ProcessorEntry entry : this.logHandler.getConfig().getProcessors()) {
            Processor processor = createProcessorInstance(entry.getName(), entry.getParameters());
            if (processor != null && !isProcessorAlreadyRegistered(processor)) {
                processors.add(processor);
            }
        }
    }

    private Thread initiateProcessingThread(){
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String event = processingQueue.take();
                    event = processEvent(event);
                    writeEventToQueue(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
        return thread;
    }

    private Processor createProcessorInstance(String className, Map<String, Object> parameters) {
        try {
            String packagePrefix = "com.github.eventmanager.processors.";
            Class<?> clazz = Class.forName(packagePrefix + className);

            if (clazz == MaskIPV4Address.class && parameters != null) {
                List<String> excludeRanges = (List<String>) parameters.get("excludeRanges");
                return new MaskIPV4Address(excludeRanges);
            }

            return (Processor) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isProcessorAlreadyRegistered(Processor processor) {
        return processors.stream().anyMatch(p -> p.getClass().equals(processor.getClass()));
    }

    /**
     * Creates a new Thread to write events to the log file. It will run indefinitely until it is interrupted (either
     * by calling the {@link ManagerBase#stopEventThread()} method or by the JVM shutting down).
     * The thread will write events to the log file if the queue is not empty and internal events are enabled.
     *
     * @deprecated This method is deprecated and will be removed in a future release, as it was not intended to be
     * used by external classes... there was also a typo. Use {@link ManagerBase#initiatEventThread()} instead.
     * */
    @Deprecated
    public Thread initiatEventThread() {
        Thread thread = new Thread(() -> {
            try {
                // Run indefinitely until the thread is interrupted
                while (!Thread.currentThread().isInterrupted()) {
                    String event = eventQueue.take();
                    writeEventToLogFile(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
        return thread;
    }

    protected Thread initiateEventThread() {
        Thread thread = new Thread(() -> {
            try {
                // Run indefinitely until the thread is interrupted
                while (!Thread.currentThread().isInterrupted()) {
                    String event = eventQueue.take();
                    writeEventToLogFile(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
        return thread;
    }

    /**
     * Stops the event thread by interrupting it and waiting for it to finish. This method should be called before
     * shutting down the application to ensure that all events are written to the log file.
     */
    public abstract void stopEventThread();

    /**
     * Writes the given event to the log file. If the event is not empty, it will be written to the log file specified
     * in the configuration file. If the log file does not exist, it will be created. If the log file cannot be created
     * or written to, an error message will be printed to the console.
     *
     * @param event the event to write to the log file.
     * */
    protected abstract void writeEventToLogFile(String event);

    /**
     * Sets the metadata fields for the event.
     *
     * @param level the log level of the event.
     * @return a map containing the metadata fields.
     */
    protected Map<String, String> setMetaDataFields(String level) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String className = stackTraceElement[4].getClassName();
        String methodName = stackTraceElement[4].getMethodName();
        int lineNumber = stackTraceElement[4].getLineNumber();

        String time = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(this.logHandler.getConfig().getEvent().getTimeFormat()));

        Map<String, String> metaDataFields = new HashMap<>();
        metaDataFields.put("time", time);
        metaDataFields.put("level", level);
        metaDataFields.put("className", className);
        metaDataFields.put("methodName", methodName);
        metaDataFields.put("lineNumber", String.valueOf(lineNumber));

        return metaDataFields;
    }

    /**
     * Logs a message to the destination file.
     *
     * @param level   the log level of the message.
     * @param message the object to log.
     */
    protected void logMessage(String level, Object message) {
        String formattedMessage;
        if (message instanceof Exception) {
            formattedMessage = ((Exception) message).getMessage();
        } else {
            formattedMessage = message.toString();
        }

        Map<String, String> metaData = setMetaDataFields(level);
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
     * Writes the given event to the event queue. This method is thread-safe and can be called from multiple threads.
     *
     * @param event the event to write to the queue.
     * */
    protected void writeEventToQueue(String event) {
        this.eventQueue.add(event);
    }

    protected void writeEventToProcessingQueue(String event) {
        this.processingQueue.add(event);
    }
}
