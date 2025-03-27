package com.github.eventmanager.helpers;

import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.OutputEntry;
import com.github.eventmanager.outputs.LogOutput;
import com.github.eventmanager.outputs.Output;
import com.github.eventmanager.outputs.PrintOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputHelper {
    protected List<Output> outputs = new ArrayList<>();
    private LogHandler logHandler;

    public OutputHelper(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    /**
     * Creates a new Output instance based on the given class name and parameters.
     *
     * @param className the name of the Output class.
     * @param parameters the parameters to pass to the Output.
     * @return a new Output instance, or null if the Output could not be created.
     */
    private Output createOutputInstance(String className, Map<String, Object> parameters) {
        try {
            String packagePrefix = "com.github.eventmanager.outputs.";
            Class<?> clazz = Class.forName(packagePrefix + className);

            Output outputInstance = getOutput(parameters, clazz);
            if (outputInstance != null) return outputInstance;

            return (Output) clazz.getDeclaredConstructor().newInstance();
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
    private Output getOutput(Map<String, Object> parameters, Class<?> clazz) {
        if(parameters == null) return null;

        if (clazz == PrintOutput.class) {
            return new PrintOutput();
        } else if (clazz == LogOutput.class) {
            return new LogOutput();
        }
        return null;
    }

    /**
     * Checks if a Processor is already registered.
     *
     * @param output the processor to check.
     * */
    private boolean isOutputAlreadyRegistered(Output output) {
        return outputs.stream().anyMatch(p -> p.getClass().equals(output.getClass()));
    }

    /**
     * Initialises all output destinations.
     * */
    public void initialiseOutputs(){
        for (OutputEntry entry : this.logHandler.getConfig().getOutputs()) {
            Output outputInstance = createOutputInstance(entry.getName(), entry.getParameters());
            if (outputInstance != null && !isOutputAlreadyRegistered(outputInstance)) {
                outputs.add(outputInstance);
            }
        }
    }

    /**
     * Output the internal events to all output destinations.
     * */
    public void outputEvent(String event) {
        for (Output output : outputs) {
            output.write(logHandler, event);
        }
    }

    /**
     * Output the event to all output destinations.
     * */
    public void outputEvent(InternalEventManager internalEventManager, String event) {
        for (Output output : outputs) {
            output.write(internalEventManager, event);
        }
    }
}
