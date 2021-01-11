/**
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * Util class for commonly used math operations.
 * </p>
 */
public class MathUtil {

    private static final Random random = new Random();

    /**
     * Get a random number within a range
     *
     * @param max maximum value
     * @return a random integer within the specified range
     */
    public static int rInt(int max) {
        return random.nextInt(max);
    }

    /**
     * Get a random number within a range
     *
     * @param min minimum value
     * @param max maximum value
     * @return a random integer within the specified range
     * @throws IllegalArgumentException when min is greater than max
     */
    public static int rInt(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("Min may not be greater than max!");
        return min + rInt(1 + max - min);
    }

    /**
     * Get a random number within a range
     *
     * @param max maximum value
     * @return a random double within the specified range
     */
    public static double rDouble(double max) {
        return max * random.nextDouble();
    }

    /**
     * Get a random number within a range
     *
     * @param min minimum value
     * @param max maximum value
     * @return a random double within the specified range
     * @throws IllegalArgumentException when min is greater than max
     */
    public static double rDouble(double min, double max) {
        if (min >= max)
            throw new IllegalArgumentException("Min may not be greater than max!");
        return min + (max - min) * random.nextDouble();
    }

    /**
     * Get the offset between two {@link org.bukkit.Location} objects
     * in a 3d environment
     *
     * @param pointA first location
     * @param pointB second location
     * @return the offset (distance) between the given locations
     */
    public static double offset(Location pointA, Location pointB) {
        return offset(pointA.toVector(), pointB.toVector());
    }

    /**
     * Get the offset between two {@link org.bukkit.Location} objects
     * in a 3d environment using {@link Vector} objects.
     *
     * @param pointA first location vector
     * @param pointB second location vector
     * @return the offset (distance) between the given locations
     */
    public static double offset(Vector pointA, Vector pointB) {
        return pointA.clone().subtract(pointB).length();
    }

    /**
     * Get the offset between two {@link org.bukkit.Location} object
     * in a 2d environment
     *
     * @param pointA first location
     * @param pointB second location
     * @return the offset (distance) between the given locations
     */
    public static double offset2d(Location pointA, Location pointB) {
        return offset2d(pointA.toVector(), pointB.toVector());
    }

    /**
     * Get the offset between two {@link org.bukkit.Location} objects
     * in a 2d environment using {@link Vector} objects.
     *
     * @param pointA first location
     * @param pointB second location
     * @return the offset (distance) between the given locations
     */
    public static double offset2d(Vector pointA, Vector pointB) {
        pointA.setY(0);
        pointB.setY(0);
        return offset(pointA, pointB);
    }

    /**
     * Get a random object from an array
     *
     * @param array the array that should be used
     * @return a random element from the specified array
     */
    public static <T> T randomElement(T[] array) {
        if(array.length < 1) return null;
        return array[rInt(array.length)];
    }

    /**
     * Get a random object from a list
     *
     * @param list the list that should be used
     * @return a random element from the specified list
     */
    public static <T> T randomElement(List<T> list) {
        if(list.size() < 1) return null;
        return list.get(rInt(list.size()));
    }

    /**
     * Round a double value
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
     * Convert a number of bytes to a human-readable value.
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
     * Test whether a given number is within a range
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