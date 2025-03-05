package com.github.eventmanager.processors;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MaskIPV4Address class implements the Processor interface and provides methods to mask IPv4 addresses in
 * different event formats (KV, JSON, XML). The class also provides methods to check if an IP address is in a CIDR
 * range and to convert an IP address to a long value. The class uses a list of CIDR ranges to mask the IP addresses.
 */
public class MaskIPV4Address implements Processor {
    private List<String> ipAddressRanges;

    public MaskIPV4Address(List<String> ipAddressRanges) {
        this.ipAddressRanges = ipAddressRanges != null ? ipAddressRanges : new ArrayList<>();
    }

    /**
     * Processes a key-value formatted event and masks any IPv4 addresses that match the CIDR ranges.
     *
     * @param event the key-value formatted event string.
     * @return the event string with masked IPv4 addresses.
     */
    @Override
    public String processKV(String event) {
        return maskIpInEvent(event, "ip=\"(\\d+\\.\\d+\\.\\d+\\.\\d+)\"");
    }

    /**
     * Processes a JSON formatted event and masks any IPv4 addresses that match the CIDR ranges.
     *
     * @param event the JSON formatted event string.
     * @return the event string with masked IPv4 addresses.
     */
    @Override
    public String processJSON(String event) {
        return maskIpInEvent(event, "\"ip\":\\s+\"(\\d+\\.\\d+\\.\\d+\\.\\d+)\"");
    }

    /**
     * Processes an XML formatted event and masks any IPv4 addresses that match the CIDR ranges.
     *
     * @param event the XML formatted event string.
     * @return the event string with masked IPv4 addresses.
     */
    @Override
    public String processXML(String event) {
        return maskIpInEvent(event, "<ip>(\\d+\\.\\d+\\.\\d+\\.\\d+)</ip>");
    }

    /**
     * Masks IPv4 addresses in the event string that match the given regular expression.
     *
     * @param event the event string.
     * @param regex the regular expression to find IPv4 addresses.
     * @return the event string with masked IPv4 addresses.
     */
    private String maskIpInEvent(String event, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(event);
        StringBuffer maskedEvent = new StringBuffer();

        // Iterate through all matches and mask the IP addresses
        while (matcher.find()) {
            String ip = matcher.group(1);
            if (isIpInAnyCidrRange(ip)) {
                matcher.appendReplacement(maskedEvent, matcher.group().replace(ip, "***.***.***.***"));
            }
        }
        matcher.appendTail(maskedEvent);
        return maskedEvent.toString();
    }

    /**
     * Checks if the given IP address is in any of the CIDR ranges.
     *
     * @param ip the IP address to check.
     * @return true if the IP address is in any of the CIDR ranges, false otherwise.
     */
    private boolean isIpInAnyCidrRange(String ip) {
        for (String cidr : ipAddressRanges) {
            if (isIpInCidr(ip, cidr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given IP address is in the specified CIDR range.
     *
     * @param ip the IP address to check.
     * @param cidr the CIDR range to check against.
     * @return true if the IP address is in the CIDR range, false otherwise.
     */
    public static boolean isIpInCidr(String ip, String cidr) {
        try {
            String[] parts = cidr.split("/");
            String networkIp = parts[0];
            int prefix = Integer.parseInt(parts[1]);

            long ipValue = ipToLong(ip);
            long networkValue = ipToLong(networkIp);

            // Create the subnet mask
            long mask = (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;

            // Check if the IP address is in the subnet
            return (ipValue & mask) == (networkValue & mask);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Converts an IPv4 address to a long value.
     *
     * @param ip the IPv4 address to convert.
     * @return the long value of the IPv4 address.
     * @throws UnknownHostException if the IP address is invalid.
     */
    private static long ipToLong(String ip) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(ip);
        byte[] bytes = inetAddress.getAddress();
        return ((bytes[0] & 0xFFL) << 24) |
                ((bytes[1] & 0xFFL) << 16) |
                ((bytes[2] & 0xFFL) << 8)  |
                (bytes[3] & 0xFFL);
    }
}