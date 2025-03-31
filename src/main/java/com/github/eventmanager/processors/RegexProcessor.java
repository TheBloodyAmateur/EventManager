package com.github.eventmanager.processors;

import com.github.eventmanager.filehandlers.config.RegexEntry;

import java.util.List;

/**
 * The RegexProcessor class is responsible for processing events using regular expressions. The class contains a list of
 * RegexEntry objects, each of which specifies a field name, a regular expression, and a replacement string. This
 * objects can be set in the configuration file or during runtime.
 */
public class RegexProcessor implements Processor{
    private List<RegexEntry> regexEntries;

    public RegexProcessor(List<RegexEntry> regexEntries) {
        this.regexEntries = regexEntries != null ? regexEntries : List.of();
    }

    @Override
    public String processKV(String event) {
        return processEvent(event, "KV");
    }

    @Override
    public String processJSON(String event) {
        //Replace all white spaces in the event
        event = event.replaceAll("\\s+", "");
        return processEvent(event, "JSON");
    }

    @Override
    public String processXML(String event) {
        return processEvent(event, "XML");
    }

    /**
     * Processes the specified event using the specified format.
     *
     * @param event the event to process.
     * @param format the format to use for processing the event.
     * @return the processed event.
     */
    private String processEvent(String event, String format) {
        for (RegexEntry regexEntry : regexEntries) {
            String regex = processRegex(format, regexEntry.getFieldName(), regexEntry.getRegex());
            String replacement = processRegex(format, regexEntry.getFieldName(), regexEntry.getReplacement());
            event = event.replaceAll(regex, replacement);
        }
        return event;
    }

    /**
     * Processes the specified value using the specified format.
     *
     * @param format the format to use for processing the value.
     * @param fieldName the name of the field to process.
     * @param value the value to process.
     * @return the processed value.
     */
    private String processRegex(String format, String fieldName, String value) {
        return switch (format){
          case "KV" -> fieldName+"=\""+ value+"\"";
          case "JSON" -> "\""+ fieldName+ "\":\"" + value+"\"";
          case "XML" -> "<"+ fieldName+">"+ value+"</"+ fieldName+">";
          default -> "N\\A";
        };
    }
}
