package com.github.eventmanager.processors;

import java.util.List;

/**
 * The FilterProcessor class is responsible for filtering out events based on a specified term.
 *
 * <p>It filters out events that do not contain the specified term.</p>
 */
public class FilterProcessor implements Processor {
    private final List<String> termToFilter;

    public FilterProcessor(List<String> termToFilter) {
        this.termToFilter = termToFilter;
    }

    @Override
    public String processKV(String event) {
        return getEvent(event);
    }


    @Override
    public String processJSON(String event) {
        return getEvent(event);
    }

    @Override
    public String processXML(String event) {
        return getEvent(event);
    }
    private String getEvent(String event) {
        for (String term : termToFilter) {
            if (!event.contains(term)) {
                return event;
            }
        }
        return "";
    }
}
