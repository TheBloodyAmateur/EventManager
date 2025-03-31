package com.github.eventmanager.helpers;

import com.github.eventmanager.filehandlers.LogHandler;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class responsible for constructing event metadata.
 * <p>
 * Event metadata typically contains contextual information such as:
 * <ul>
 *     <li>Timestamp indicating when the event occurred.</li>
 *     <li>The log level associated with the event.</li>
 *     <li>Class name where the log method was invoked.</li>
 *     <li>Method name from which the event was logged.</li>
 *     <li>The exact line number within the source file.</li>
 * </ul>
 * <p>
 * This metadata aids in debugging, troubleshooting, and providing detailed logging information.
 */
public class EventMetaDataBuilder {
    /**
     * Constructs a metadata map containing contextual information about a logged event.
     *
     * @param level      the severity or informational level of the logged event (e.g., INFO, ERROR, DEBUG).
     * @param logHandler the {@link LogHandler} instance providing configuration, such as timestamp formatting details.
     * @return a {@link Map} containing event metadata with keys: "time", "level", "className", "methodName", and "lineNumber".
     */
    public static Map<String, String> buildMetaData(String level, LogHandler logHandler) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];

        String time = ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern(logHandler.getConfig().getEvent().getTimeFormat()));

        Map<String, String> metaData = new HashMap<>();
        metaData.put("time", time);
        metaData.put("level", level);
        metaData.put("className", element.getClassName());
        metaData.put("methodName", element.getMethodName());
        metaData.put("lineNumber", String.valueOf(element.getLineNumber()));
        return metaData;
    }
}
