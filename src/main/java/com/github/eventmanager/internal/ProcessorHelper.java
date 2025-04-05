package com.github.eventmanager.internal;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.ProcessorEntry;
import com.github.eventmanager.filehandlers.config.RegexEntry;
import com.github.eventmanager.processors.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProcessorHelper {
    @Getter
    @Setter
    protected List<Processor> processors = new ArrayList<>();
    private LogHandler logHandler;

    public ProcessorHelper(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    /**
     * Creates a new Processor instance based on the given class name and parameters.
     *
     * @param className the name of the Processor class.
     * @param parameters the parameters to pass to the Processor.
     * @return a new Processor instance, or null if the Processor could not be created.
     */
    private Processor createProcessorInstance(String className, Map<String, Object> parameters) {
        try {
            String packagePrefix = "com.github.eventmanager.processors.";
            Class<?> clazz = Class.forName(packagePrefix + className);

            Processor excludeRanges = getProcessor(parameters, clazz);
            if (excludeRanges != null) return excludeRanges;

            return (Processor) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a Processor instance based on the given parameters and class.
     *
     * @param parameters the parameters to pass to the Processor.
     * @param clazz the class of the Processor.
     * */
    private Processor getProcessor(Map<String, Object> parameters, Class<?> clazz) {
        if(parameters == null) return null;

        if (clazz == MaskIPV4Address.class) {
            List<String> excludeRanges = (List<String>) parameters.get("excludeRanges");
            return new MaskIPV4Address(excludeRanges);
        } else if (clazz == EnrichingProcessor.class) {
            List<String> enrichingFields = (List<String>) parameters.get("enrichingFields");
            return new EnrichingProcessor(enrichingFields);
        } else if (clazz == RegexProcessor.class) {
            List<RegexEntry> regexEntries = (List<RegexEntry>) parameters.get("regexEntries");
            return new RegexProcessor(regexEntries);
        } else if (clazz == FilterProcessor.class) {
            List<String> termToFilter = (List<String>) parameters.get("termToFilter");
            return new FilterProcessor(termToFilter);
        }
         // Add more processor types as needed
        return null;
    }

    /**
     * Checks if a Processor is already registered.
     *
     * @param processor the processor to check.
     * */
    private boolean isProcessorAlreadyRegistered(Processor processor) {
        return processors.stream().anyMatch(p -> p.getClass().equals(processor.getClass()));
    }

    /**
     * Processes an event by passing it through all registered processors.
     *
     * @param event the event to process.
     * @return the processed event.
     * */
    public String processEvent(String event) {
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

    /**
     * Initialises all processors.
     * */
    public void initialiseProcessors(){
        for (ProcessorEntry entry : this.logHandler.getConfig().getProcessors()) {
            Processor processor = createProcessorInstance(entry.getName(), entry.getParameters());
            if (processor != null && !isProcessorAlreadyRegistered(processor)) {
                processors.add(processor);
            }
        }
    }

    public boolean addProcessor(ProcessorEntry processorEntry) {
        if (processorEntry == null) return false;
        Processor processor = createProcessorInstance(processorEntry.getName(), processorEntry.getParameters());
        if (processor != null && !isProcessorAlreadyRegistered(processor)) {
            processors.add(processor);
            return true;
        }
        return false;
    }

    public boolean removeProcessor(String processorName) {
        if (processorName == null) return false;
        for (Processor processor : processors) {
            if (processor.getClass().getSimpleName().equalsIgnoreCase(processorName)) {
                processors.remove(processor);
                return true;
            }
        }
        return false;
    }

    public boolean removeProcessor(ProcessorEntry processorEntry) {
        if (processorEntry == null) return false;
        for (Processor processor : processors) {
            // Generate a object of the same type as the processorEntry
            Processor outputInstance = getProcessor(processorEntry.getParameters(), processor.getClass());
            if (processor.getClass().equals(outputInstance.getClass())) {
                processors.remove(processor);
                return true;
            }
        }
        return false;
    }
}
