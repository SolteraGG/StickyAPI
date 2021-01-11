/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Provides extra functionality for Java Number classes.
 * </p>
 */
public final class NumberUtil {
    private NumberUtil() {
    }

    /**
     * <p>
     * Checks if the String contains only unicode digits. A decimal point is not a
     * unicode digit and returns false.
     * </p>
     *
     * <p>
     * <code>null</code> will return <code>false</code>. An empty String
     * (length()=0) will return <code>true</code>.
     * </p>
     *
     * <pre>
     * NumberUtil.isNumeric(null)   = false
     * NumberUtil.isNumeric("")     = true
     * NumberUtil.isNumeric("  ")   = false
     * NumberUtil.isNumeric("123")  = true
     * NumberUtil.isNumeric("12 3") = false
     * NumberUtil.isNumeric("ab2c") = false
     * NumberUtil.isNumeric("12-3") = false
     * NumberUtil.isNumeric("12.3") = false
     * </pre>
     * <p>
     * Returns <code>true</code> if only contains digits, and is non-null
     * 
     * @param string the String to check, may be null
     * @return {@link Boolean}
     */
    public static boolean isNumeric(@NotNull String string) {
        int size = string.length();
        for (int i = 0; i < size; i++) {
            if (Character.isDigit(string.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a number as the percentage of another.
     * 
     * @param x      The number who's percentage of the total this method will
     *               return
     * @param total  The total
     * @param string If this should return as a string with `%` appended to the end
     * @return {@link Double}
     */
    public static String getPercentage(@NotNull double x, @NotNull double total, @NotNull Boolean string) {
        var percent = (x / total);
        StringBuilder sb = new StringBuilder();
        if (string) {
            sb.append((percent * 100) + "%");
        } else {
            sb.append((percent));
        }
        return sb.toString();
    }

    /**
     * Get a number as the percentage of another.
     * 
     * @param x     The number who's percentage of the total this method will return
     * @param total The total
     * @return {@link Double}
     */
    public static Double getPercentage(@NotNull int x, @NotNull int total) {
        return Double.valueOf(getPercentage(x, total, false));
    }

    /**
     * Get a number as the percentage of another.
     * 
     * @param x     The number who's percentage of the total this method will return
     * @param total The total
     * @return {@link String}
     */
    public static String getPercentageString(@NotNull int x, @NotNull int total) {
        return getPercentage(x, total, true);
    }

    /**
     * Get a random number within a range
     * <p>
     * Returns a random integer within the specified range
     * 
     * @param min minimum value
     * @param max maximum value
     * @return {@link Integer}
     * @throws IllegalArgumentException when min is greater than max
     */
    public static int getRandomNumber(@NotNull int min, @NotNull int max) {
        if (min >= max)
            throw new IllegalArgumentException("Min may not be greater than max!");
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Try to return long as an int, capped at int max and int min
     * <p>
     * Returns the long as a capped int
     * 
     * @param lng The long to convert
     * @return {@link Integer}
     */
    public static int longToInt(@NotNull long lng) {
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
