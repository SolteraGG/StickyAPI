/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import com.google.common.base.Preconditions;

/**
 * Utility class for generating ordinals e.g. `1st, 2nd, 3rd`
 */
public final class Ordinal {
	private Ordinal() {}

	/**
	 * Get the appropriate ordinal for the
	 * @param number The number to fetch the ordinal for
	 * @return A {@link String} containing the appropriate ordinal.
	 */
	public static String getOrdinal(int number) {
		Preconditions.checkArgument(number > 0);
		// store modulo 10 and 100 of target - useful for calculating things
		int mod10 = number % 10;
		int mod100= number % 100;
		// st is used with numbers ending in 1 (e.g. 1st, pronounced first)
		if (mod10 == 1 && mod100 != 11) {
			return "st";
		}
		// nd is used with numbers ending in 2 (e.g. 92nd, pronounced ninety-second)
		if (mod10 == 2 && mod100 != 12) {
			return "nd";
		}
		// rd is used with numbers ending in 3 (e.g. 33rd, pronounced thirty-third)
		// As an exception to the above rules, all the "teen" numbers ending with 11, 12 or 13 use -th (e.g. 11th, pronounced eleventh, 112th, pronounced one hundred [and] twelfth)
		if (mod10 == 3 && mod100 != 13) {
			return "rd";
		}
		// th is used for all other numbers (e.g. 9th, pronounced ninth).
		return "th";
	}

	/**
	 * Return a number formatted with its ordinal.
	 * @param number The number to format with its ordinal
	 * @return A {@link String} containing the number with its ordinal.
	 */
	public static String withOrdinal(int number) {
		return number + getOrdinal(number);
	}
}
