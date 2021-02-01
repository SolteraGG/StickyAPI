/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

/**
 * Utility class for IP Addresses
 */
@UtilityClass
public final class IPUtil {
	/**
	 * Check if an IP Address is within a CIDR range
	 * <p>
	 * true if IP is in the CIDR range, otherwise false
	 * 
	 * @param address1 The IP Address to check
	 * @param address2 The subnet to check against
	 * @return {@link Boolean}
	 */
	public static Boolean isContainedBy(@NotNull String address1, @NotNull String address2) {

		IPAddress subnetAddress = new IPAddressString(address2).getAddress();
		if (subnetAddress == null)
			return false; // prevent NPE for invalid subnetAddress

		IPAddress subnet = subnetAddress.toPrefixBlock();
		IPAddress testAddress = new IPAddressString(address1).getAddress();
		if (testAddress == null)
			return false; // prevent NPE for invalid testAddress

		return subnet.contains(testAddress);
	}

	/**
	 * Check if an IP address is valid
	 * 
	 * @param address The IP address to check
	 * @return {@link Boolean}
	 * @since 2.0
	 */
	public static Boolean isValid(@NotNull String address) {
		return new IPAddressString(address).isValid();
	}
}
