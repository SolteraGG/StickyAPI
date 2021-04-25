/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import com.dumbdogdiner.stickyapi.math.vector.Vector2;
import com.dumbdogdiner.stickyapi.math.vector.Vector3;
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
	 * Get a random number between 0 and the specified maximum value (exclusive).
	 *
	 * @param max The maximum value
	 * @return A random integer between 0 and the specified maximum value.
	 */
	public static int randomInt(int max) {
		Preconditions.checkArgument(max >= 0, "Argument must be positive");
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
		// add 1 here to ensure inclusivity
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
		// don't need to add 1 here since doubles will be inclusive as they
		// are continuous, unlike integers.
		return min + randomDouble(max - min);
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

	/**
	 * @return A random angle between 0 and 2pi.
	 */
	public static double randomAngle() {
		return randomDouble(0, Math.PI * 2);
	}

	/**
	 * @return A random angle between -pi and pi.
	 */
	public static double randomDualAngle() {
		return randomDouble(-Math.PI, Math.PI);
	}

	/**
	 * Return a random {@link Vector2} with magnitude <code>r</code>.
	 * @param r The radius, or magnitude of the vector
	 * @return A random {@link Vector2} with magnitude <code>r</code>.
	 */
	public static Vector2 randomVector2(double r) {
		return Vector2.fromPolar(r, randomAngle());
	}

	/**
	 * Return a random unit {@link Vector2}. This vector will always
	 * have a magnitude of <code>1</code>.
	 * @return A random {@link Vector2} with magnitude 1.
	 */
	public static Vector2 randomVector2() {
		return randomVector2(1);
	}

	/**
	 * Return a random {@link Vector3} with magnitude <code>r</code>.
	 * @param r The radius, or magnitude of the vector
	 * @return A random {@link Vector3} with magnitude <code>r</code>.
	 */
	public static Vector3 randomVector3(double r) {
		return Vector3.fromPolar(r, randomAngle(), randomAngle());
	}

	/**
	 * Return a random unit {@link Vector3}. This vector will always
	 * have a magnitude of <code>1</code>.
	 * @return A random {@link Vector3} with magnitude 1.
	 */
	public static Vector3 randomVector3() {
		return randomVector3(1);
	}
}
