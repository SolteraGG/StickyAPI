package com.dumbdogdiner.stickyapi.common.util;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

public class IPUtil {
    /**
	 * Check if an IP falls within a CIDR range
	 * @param ipAddress1 IP Address to check
	 * @param ipAddress2 The second IP Address to check
	 * @param prefix The prefix to check against
	 * @return true if IP is in prefix, otherwise false
	 */
	public static Boolean checkRange(String ipAddress1, String ipAddress2, String prefix) {
		
		String subnetStr = ipAddress1+"/"+prefix;
		IPAddress subnetAddress = new IPAddressString(subnetStr).getAddress();
		if (subnetAddress == null) return false; // prevent NPE for invalid subnetAddress
		IPAddress subnet = subnetAddress.toPrefixBlock();
		IPAddress testAddress = new IPAddressString(ipAddress2).getAddress();
		if (testAddress == null) return false; // prevent NPE for invalid testAddress
		return subnet.contains(testAddress);
	}
}