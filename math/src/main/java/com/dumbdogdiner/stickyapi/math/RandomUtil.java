/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Utility class for dealing with randomness.
 * TODO: Consistency issues - randomInt(int max) is exclusive, randomInt(int max, int min) isn't.
 */
public class RandomUtil {
	private RandomUtil() {
	}

	private static final Random random = new Random();

	/**
	 * Get a random number between 0 and the specified maximum value.
	 *
	 * @param max The maximum value
	 * @return A random integer between 0 and the specified maximum value.
	 */
	public static int randomInt(int max) {
		return random.nextInt(max);
	}

	/**
	 * Get a random number within a range.
	 *
	 * @param min The minimum value (inclusive)
	 * @param max The maximum value (inclusive)
	 * @return A random integer within the specified range
	 * @throws IllegalArgumentException if min is greater than max.
	 */
	public static int randomInt(int min, int max) {
		// validate argument
		Preconditions.checkArgument(min <= max, "The minimum value may not be greater than the maximum.");
		// exception case
		if (min == max)
			return min;
		return min + randomInt(1 + max - min);
	}

	/**
	 * Get a random number between 0 and the specified maximum value.
	 *
	 * @param max maximum value
	 * @return A random double between 0 and the specified maximum value.
	 */
	public static double randomDouble(double max) {
		return max * random.nextDouble();
	}

	/**
	 * Get a random double within a range
	 *
	 * @param min minimum value
	 * @param max maximum value
	 * @return a random double within the specified range
	 * @throws IllegalArgumentException when min is greater than max
	 */
	public static double randomDouble(double min, double max) {
		// validate argument
		Preconditions.checkArgument(min <= max, "The minimum value may not be greater than the maximum.");
		// exception case
		if (min == max)
			return min;
		return min + (1 + max - min) * random.nextDouble();
	}

	/**
	 * Get a random element from the target array.
	 *
	 * @param array The target array
	 * @return A random element from the target array.
	 */
	public static <T> @Nullable T randomElement(@NotNull T @NotNull [] array) {
		// exception - array is empty
		if (array.length < 1) return null;
		// pick random index, return element at index
		return array[randomInt(array.length)];
	}

	/**
	 * Get a random element from the target list.
	 *
	 * @param list The target list
	 * @return A random element from the target list.
	 */
	public static <T> @Nullable T randomElement(@NotNull List<T> list) {
		// exception - list is empty
		if (list.isEmpty()) return null;
		// pick random index, return element at index
		return list.get(randomInt(list.size()));
	}

	/**
	 * Round a double value.
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
	 * Convert a number of bytes to a human-readable value.
	 * TODO: How the absolute fuck does this work?
	 *
	 * @param bytes the value that should be converted
	 * @return a human-readable byte value
	 */
	public static String bytesToReadable(long bytes) {
		long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
		if (absB < 1024) {
			return bytes + " B";
		}
		long value = absB;
		CharacterIterator ci = new StringCharacterIterator("KMGTPE");
		for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
			value >>= 10;
			ci.next();
		}
		value *= Long.signum(bytes);
		return String.format("%.1f %ciB", value / 1024.0, ci.current());
	}



	/**
	 * @see #randomElement(List)
	 * Primitive values cannot be used with type generics, so these methods exist for ease of use.
	 * @since 3.0
	 */
	public static char randomElement(char [] choices) {
		return Objects.requireNonNull(randomElement(Chars.asList(choices)));
	}

	/**
	 * @see #randomElement(List)
	 * Primitive values cannot be used with type generics, so these methods exist for ease of use.
	 * @since 3.0
	 */
	public static int randomElement(int [] choices) {
		return Objects.requireNonNull(randomElement(Ints.asList(choices)));
	}
}
