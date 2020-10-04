/* 
 *  Koffee - A simple collection of utilities I commonly use
 *  Copyright (C) 2019-2020 Justin Crawford <justin@Stacksmash.net>
 *  Copyright (C) 2019-2020 Zachery Coleman <Zachery@Stacksmash.net>
 *  Copyright (C) 2019-2020 Skye Elliot <actuallyori@gmail.com>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ristexsoftware.koffee.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Get the percentage of two numbers
     * @param x First number
     * @param y Second number
     * @param decimalPlace
     * @return
     */
    public static Float getPercentage(int x, int y, Double decimalPlace) {
        Float z = Float.valueOf(String.valueOf(y) + ".0f");
        final DecimalFormat df = new DecimalFormat(String.valueOf(decimalPlace));
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return Float.valueOf(df.format((x/z)*100));
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static int ceil(final double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static int round(double num) {
        return floor(num + 0.5d);
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static double square(double num) {
        return num * num;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static int toInt(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static float toFloat(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static double toDouble(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static long toLong(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static short toShort(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static byte toByte(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static boolean isFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static boolean isFinite(float f) {
        return Math.abs(f) <= Float.MAX_VALUE;
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static void checkFinite(double d, @NotNull String message) {
        if (!isFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @deprecated This will be removed in a future release
     */
    public static void checkFinite(float d, @NotNull String message) {
        if (!isFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }
}
