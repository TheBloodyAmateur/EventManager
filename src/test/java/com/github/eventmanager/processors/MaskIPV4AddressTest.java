package com.github.eventmanager.processors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MaskIPV4AddressTest {

    private MaskIPV4Address maskIPV4Address;

    @BeforeEach
    void setUp() {
        maskIPV4Address = new MaskIPV4Address(Arrays.asList("192.168.1.0/24", "10.0.0.0/8"));
    }

    @Test
    void processKV() {
        String event = "user=JohnDoe ip=\"192.168.1.100\" action=login";
        String expected = "user=JohnDoe ip=\"***.***.***.***\" action=login";
        assertEquals(expected, maskIPV4Address.processKV(event));
    }

    @Test
    void processJSON() {
        String event = "{\"user\": \"JohnDoe\", \"ip\": \"10.0.0.1\", \"action\": \"login\"}";
        String expected = "{\"user\": \"JohnDoe\", \"ip\": \"***.***.***.***\", \"action\": \"login\"}";
        assertEquals(expected, maskIPV4Address.processJSON(event));
    }

    @Test
    void processXML() {
        String event = "<event><user>JohnDoe</user><ip>192.168.1.100</ip><action>login</action></event>";
        String expected = "<event><user>JohnDoe</user><ip>***.***.***.***</ip><action>login</action></event>";
        assertEquals(expected, maskIPV4Address.processXML(event));
    }

    @Test
    void processKV_noMatch() {
        String event = "user=JohnDoe ip=\"172.16.0.1\" action=login";
        assertEquals(event, maskIPV4Address.processKV(event));
    }

    @Test
    void processJSON_noMatch() {
        String event = "{\"user\": \"JohnDoe\", \"ip\": \"172.16.0.1\", \"action\": \"login\"}";
        assertEquals(event, maskIPV4Address.processJSON(event));
    }

    @Test
    void processXML_noMatch() {
        String event = "<event><user>JohnDoe</user><ip>172.16.0.1</ip><action>login</action></event>";
        assertEquals(event, maskIPV4Address.processXML(event));
    }
}