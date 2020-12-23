/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.util;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

/**
 * Utility class for IP Addresses
 */
public final class IPUtil {

    private IPUtil() {}

    /**
     * Check if two IP addresses are within the same CIDR range
     * <p>
     * true if IP is in prefix, otherwise false
     *
     * @param address1 The first IP address
     * @param address2 The IP to compare against the first addresses range
     * @param prefix   The prefix to check against
     * @return {@link java.lang.Boolean}
     */
    public static Boolean compareRangeCIDR(
        String address1,
        String address2,
        String prefix
    ) {
        IPAddress subnetAddress = new IPAddressString(address1 + "/" + prefix)
        .getAddress();
        if (subnetAddress == null) return false; // prevent NPE for invalid subnetAddress

        IPAddress subnet = subnetAddress.toPrefixBlock();
        IPAddress testAddress = new IPAddressString(address2).getAddress();
        if (testAddress == null) return false; // prevent NPE for invalid testAddress

        return subnet.contains(testAddress);
    }
}
