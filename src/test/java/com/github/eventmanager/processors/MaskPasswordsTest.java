package com.github.eventmanager.processors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaskPasswordsTest {

    @Test
    void processKV() {
        MaskPasswords maskPasswords = new MaskPasswords();
        String event = "user=JohnDoe password=\"secret\" action=login";
        String expected = "user=JohnDoe password=*** action=login";
        assertEquals(expected, maskPasswords.processKV(event));
    }

    @Test
    void processJSON() {
        MaskPasswords maskPasswords = new MaskPasswords();
        String event = "{\"user\": \"JohnDoe\", \"password\": \"secret\", \"action\": \"login\"}";
        String expected = "{\"user\": \"JohnDoe\", \"password\": \"***\", \"action\": \"login\"}";
        assertEquals(expected, maskPasswords.processJSON(event));
    }

    @Test
    void processXML() {
        MaskPasswords maskPasswords = new MaskPasswords();
        String event = "<event><user>JohnDoe</user><password>secret</password><action>login</action></event>";
        String expected = "<event><user>JohnDoe</user><password>***</password><action>login</action></event>";
        assertEquals(expected, maskPasswords.processXML(event));
    }

    @Test
    void processKV_noPassword() {
        MaskPasswords maskPasswords = new MaskPasswords();
        String event = "user=JohnDoe action=login";
        assertEquals(event, maskPasswords.processKV(event));
    }

    @Test
    void processJSON_noPassword() {
        MaskPasswords maskPasswords = new MaskPasswords();
        String event = "{\"user\": \"JohnDoe\", \"action\": \"login\"}";
        assertEquals(event, maskPasswords.processJSON(event));
    }

    @Test
    void processXML_noPassword() {
        MaskPasswords maskPasswords = new MaskPasswords();
        String event = "<event><user>JohnDoe</user><action>login</action></event>";
        assertEquals(event, maskPasswords.processXML(event));
    }
}