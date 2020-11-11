/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>Provides extra functionality for Java Number classes.</p>
 */
public final class NumberUtil {
    private NumberUtil() {}
    /**
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String (length()=0) will return <code>true</code>.</p>
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
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a number as the percentage of another.
     * @param x The number who's percentage of the total this method will return
     * @param total The total
     * @param decimalPlace The number of decimal places to return
     * @return {@link Double}
     */
    public static Double getPercentage(int x, int total, Double decimalPlace) {
        var percent = ((double) x / (double) total) * 100;
        final DecimalFormat formatter = new DecimalFormat(String.valueOf(decimalPlace));
        formatter.setRoundingMode(RoundingMode.HALF_EVEN);
        return Double.valueOf(formatter.format(percent));
    }

    /**
     * Get a random number within a range
     * 
     * @param min minimum value
     * @param max maximum value
     * @return a random integer within the specified range
     * @throws IllegalArgumentException when min is greater than max
     */
    public static int getRandomNumber(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("Min may not be greater than max!");
	    return (int) ((Math.random() * (max - min)) + min);
	}

    /**
     * Try to return long as an int, capped at int max and int min
     * @param l The long to convert
     * @return Returns the long as a capped int
     */
    public static int intHelper(long l){
        try {
            return Math.toIntExact(l);
        } catch (ArithmeticException ae){
            switch(Long.compare(l, 0)){
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
