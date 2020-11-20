/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

/**
 * Utility class for IP Addresses
 */
public final class IPUtil {
	private IPUtil() {
	}

	/**
	 * Check if two IP addresses are within the same CIDR range
	 * <p>
	 * true if IP is in prefix, otherwise false
	 * 
	 * @param address1 IP Address to check
	 * @param address2 The second IP Address to check
	 * @param prefix   The prefix to check against
	 * @return {@link java.lang.Boolean}
	 */
	public static Boolean compareRangeCIDR(String address1, String address2, String prefix) {

		String subnetStr = address1 + "/" + prefix;
		IPAddress subnetAddress = new IPAddressString(subnetStr).getAddress();
		if (subnetAddress == null)
			return false; // prevent NPE for invalid subnetAddress
		IPAddress subnet = subnetAddress.toPrefixBlock();
		IPAddress testAddress = new IPAddressString(address2).getAddress();
		if (testAddress == null)
			return false; // prevent NPE for invalid testAddress
		return subnet.contains(testAddress);
	}
}
