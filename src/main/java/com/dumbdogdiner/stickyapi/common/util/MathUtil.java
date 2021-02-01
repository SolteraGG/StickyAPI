/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import lombok.experimental.UtilityClass;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * Util class for commonly used math operations.
 * </p>
 */
@UtilityClass
public class MathUtil {

    private static final Random random = new Random();

    /**
     * <p>
     * Get a random number within a range
     * </p>
     *
     * @param max maximum value
     * @return a random integer within the specified range
     */
    public static int randomInt(int max) {
        return random.nextInt(max);
    }

    /**
     * <p>
     * Get a random number within a range
     * </p>
     *
     * @param min minimum value
     * @param max maximum value
     * @return a random integer within the specified range
     * @throws IllegalArgumentException when min is greater than max
     */
    public static int randomInt(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("Min may not be greater than max!");
        return min + randomInt(1 + max - min);
    }

    /**
     * <p>
     * Get a random double within a range
     * </p>
     *
     * @param max maximum value
     * @return a random double within the specified range
     */
    public static double randomDouble(double max) {
        return max * random.nextDouble();
    }

    /**
     * <p>
     * Get a random double within a range
     * </p>
     *
     * @param min minimum value
     * @param max maximum value
     * @return a random double within the specified range
     * @throws IllegalArgumentException when min is greater than max
     */
    public static double randomDouble(double min, double max) {
        if (min >= max)
            throw new IllegalArgumentException("Min may not be greater than max!");
        return min + (max - min) * random.nextDouble();
    }

    /**
     * <p>
     * Get a random object from an array
     * </p>
     *
     * @param array the array that should be used
     * @return a random element from the specified array
     */
    public static <T> T randomElement(T[] array) {
        if(array.length < 1) return null;
        return array[randomInt(array.length)];
    }

    /**
     * <p>
     * Get a random object from a list
     * </p>
     *
     * @param list the list that should be used
     * @return a random element from the specified list
     */
    public static <T> T randomElement(List<T> list) {
        if(list.size() < 1) return null;
        return list.get(randomInt(list.size()));
    }

    /**
     * <p>
     * Round a double value
     * </p>
     *
     * @param value the value that should be rounded
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
     * <p>
     * Convert a number of bytes to a human-readable value.
     * </p>
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
     * <p>
     * Test whether a given number is within a range
     * </p>
     *
     * @param number the number that should be tested
     * @param min minimum value
     * @param max maximum value
     * @return <code>true</code> if given number is in range
     */
    public static boolean inRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

}