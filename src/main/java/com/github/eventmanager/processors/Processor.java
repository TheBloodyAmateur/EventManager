package com.github.eventmanager.processors;

/**
 * The Processor interface provides methods to process events in different formats (KV, JSON, XML).
 * */
public interface Processor {
    /**
     * Processes a key-value formatted event.
     *
     * @param event the key-value formatted event string.
     * @return the processed event string.
     */
    String processKV(String event);

    /**
     * Processes a JSON formatted event.
     *
     * @param event the JSON formatted event string.
     * @return the processed event string.
     */
    String processJSON(String event);

    /**
     * Processes an XML formatted event.
     *
     * @param event the XML formatted event string.
     * @return the processed event string.
     */
    String processXML(String event);
}
