/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Utility class for working with numbers.
 */
public final class NumberUtil {
	private NumberUtil() {}

	/**
	 * Test whether the specified number is within the specified range.
	 *
	 * @param number The specified number
	 * @param min The minimum value (inclusive)
	 * @param max The maximum value (inclusive)
	 * @return <code>true</code> if given number is in the given range.
	 */
	public static boolean inRange(Number number, Number min, Number max) {
		return number.doubleValue() >= min.doubleValue() && number.doubleValue() <= max.doubleValue();
	}

	/**
	 * Round a double value
	 *
	 * @param value  the value that should be rounded
	 * @param places amount of decimal places
	 * @return {@link Double}
	 */
	public static double round(double value, int places) {
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	/**
	 * Test if the target string can be considered an integer. Checks if every character is a unicode digit.
	 *
	 * <pre>
	 * NumberUtil.isInteger("123")         = true
	 * NumberUtil.isInteger("-123")        = true
	 * NumberUtil.isInteger(null)          = false
	 * NumberUtil.isInteger("")            = false
	 * NumberUtil.isInteger("  ")          = false
	 * NumberUtil.isInteger("12 3")        = false
	 * NumberUtil.isInteger("ab2c")        = false
	 * NumberUtil.isInteger("12-3")        = false
	 * NumberUtil.isInteger("12.3")        = false
	 * NumberUtil.isInteger("-123", false) = false
	 * </pre>
	 *
	 * @param string The string to check
	 * @param signed Whether to allow signed numbers
	 * @return <code>true</code> if the target string can be considered an integer.
	 */
	public static boolean isInteger(@Nullable String string, boolean signed) {
		// an empty or null string is very definitely not an integer
		if (string == null || string.length() == 0) {
			return false;
		}
		// sign is allowed, but the first char is neither a sign or a digit
		if (signed && !Character.isDigit(string.charAt(0)) && string.charAt(0) != '-') {
			return false;
		}
		// iterate over chars and check if they are digits
		// starts from position 1 if the string is allowed to be signed
		for (int i = signed ? 1 : 0; i < string.length(); i++) {
			if (!Character.isDigit(string.charAt(i))) {
				return false;
			}
		}
		// made it here, must be an integer
		return true;
	}

	/**
	 * Test if the target string can be considered an integer. Checks if every character is a unicode digit.
	 * Allows signed numbers.
	 * @param string The target string
	 * @return <code>true</code> if the target string can be considered a signed integer.
	 */
	public static boolean isInteger(@Nullable String string) {
		return isInteger(string, true);
	}

	// format: sign value decimal exponent
	private static final Pattern NUMERIC_REGEX = Pattern.compile("-?[0-9]+\\.?[0-9]+(?:e-?[0-9]+)?$");

	/**
	 * Checks if the string is considered numeric.
	 *
	 * @param string the String to check, may be null
	 * @return <code>true</code> if the string is numeric
	 */
	public static boolean isNumeric(@NotNull String string) {
		Preconditions.checkNotNull(string);
		return NUMERIC_REGEX.matcher(string).matches();
	}

	/**
	 * Utility method for checking if two doubles are very very almost equal. Useful for getting rid
	 * of floating point inaccuracies.
	 * @param a The first double
	 * @param b The second double
	 * @param epsilon The maximum difference between them
	 * @return <code>true</code> if the numbers are within <code>epsilon</code> of each other.
	 */
	public static boolean almostEquals(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}

	/**
	 * Utility method for checking if two doubles are very very almost equal. Useful for getting rid
	 * of floating point inaccuracies.
	 * @param a The first double
	 * @param b The second double
	 * @return <code>true</code> if the numbers are within 1 part in a million of each other.
	 */
	public static boolean almostEquals(double a, double b) {
		return almostEquals(a, b, 10e-6);
	}

	/**
	 * Get a number as the percentage of another.
	 *
	 * @param x The number who's percentage of the total this method will return
	 * @param total The total
	 * @return The percentage value
	 */
	public static double getPercentage(Number x, Number total) {
		return x.doubleValue() / total.doubleValue() * 100;
	}

	/**
	 * Get a number as the percentage of another.
	 *
	 * @param x The number who's percentage of the total this method will return
	 * @param total The total
	 * @param places The number of decimal places to round to
	 * @return A rounded percentage value
	 */
	public static double getPercentage(Number x, Number total, int places) {
		return round(getPercentage(x, total), places);
	}

	/**
	 * Get a number as the percentage of another.
	 *
	 * @param x The number who's percentage of the total this method will return
	 * @param total The total
	 * @param places The number of decimal places to return
	 * @return A formatted {@link String} containing the percentage to the specified number of decimal places.
	 */
	public static String getPercentageString(Number x, Number total, int places) {
		return getPercentage(x, total, places) + "%";
	}

	/**
	 * Get a number as the percentage of another.
	 *
	 * @param x The number who's percentage of the total this method will return
	 * @param total The total
	 * @return A formatted {@link String} containing the percentage to 2 decimal places.
	 */
	public static String getPercentageString(Number x, Number total) {
		return getPercentageString(x, total, 2);
	}

	/**
	 * Try to return long as an int, capped at int max and int min
	 *
	 * @param lng The long to convert
	 * @return {@link Integer}
	 */
	public static int longToInt(long lng) {
		try {
			return Math.toIntExact(lng);
		} catch (ArithmeticException ae) {
			switch (Long.compare(lng, 0)) {
				case 1:
					return Integer.MAX_VALUE;
				case 0:
					return 0;
				case -1:
					return Integer.MIN_VALUE;
				default:
					throw new ArithmeticException(); // Somehow Long.compare is broken?? This should be impossible
			}
		}
	}
}
