/**
 * This package contains classes that implement various event processing functionalities.
 *
 * <p>The processors in this package are responsible for handling different types of event data,
 * such as masking sensitive information (e.g., passwords, IP addresses) and enriching events with additional metadata.
 *
 * <p>Classes included in this package:
 * <ul>
 *   <li>{@link com.github.eventmanager.processors.MaskPasswords} - Masks passwords in event data.</li>
 *   <li>{@link com.github.eventmanager.processors.MaskIPV4Address} - Masks IPv4 addresses in event data.</li>
 *   <li>{@link com.github.eventmanager.processors.EnrichingProcessor} - Enriches event data with additional metadata such as hostname and IP address.</li>
 * </ul>
 *
 * <p>Each processor class implements the {@link com.github.eventmanager.processors.Processor} interface,
 * which defines methods for processing events in different formats (KV, JSON, XML).
 */
package com.github.eventmanager.processors;