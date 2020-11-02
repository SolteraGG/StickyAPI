/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
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

package com.dumbdogdiner.stickyapi.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Utility methods for dealing with time and duration parsing.
 */
public class TimeUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy HH:mm:ss");
    /**
     * Convert the unix timestamp to a human readable duration string
     * 
     * @param t {@link java.sql.Timestamp}
     * @return a human readable duration
     */
    public static String durationString(Timestamp t) {
        return TimeUtil.durationString(t.getTime());
    }


    // 1 year 1 hours and 10 minutes
    // 1.16h
    // 1 day and 16 hours


    // public static String significantDurationString(final long time) {
    //     var t = time / 1000L;
    //     long years = t / 31449600;
    //     long weeks = (t / 604800) % 52;
    //     long days = (t / 86400) % 7;
    //     long hours = (t / 3600) % 24;
    //     long minutes = (t / 60) % 60;
    //     long seconds = t % 60;

        
    // }

    /**
     * Convert the unix timestamp to a human readable duration string
     * 
     * @param t unix timestamp
     * @return human readable duration
     */
    public static String durationString(final long time) {
        var t = time / 1000L;
        long years = t / 31449600;
        long weeks = (t / 604800) % 52;
        long days = (t / 86400) % 7;
        long hours = (t / 3600) % 24;
        long minutes = (t / 60) % 60;
        long seconds = t % 60;

        List<String> components = new ArrayList<>();

        if (years != 0) {
            components.add(String.format("%d %s", years, years != 1 ? "years" : "year"));
        }
        if (weeks != 0) {
            components.add(String.format("%d %s", weeks, weeks != 1 ? "weeks" : "week"));
        }
        if (days != 0) {
            components.add(String.format("%d %s", days, days != 1 ? "days" : "day"));
        }
        if (hours != 0) {
            components.add(String.format("%d %s", hours, hours != 1 ? "hours" : "hour"));
        }
        if (minutes != 0) {
            components.add(String.format("%d %s", minutes, minutes != 1 ? "minutes" : "minute"));
        }
        if (seconds != 0) {
            components.add(String.format("%d %s", seconds, seconds != 1 ? "seconds" : "second"));
        }
        return Arrays.copyOf(components.toArray(), components.size() - 1) + " and " + components.get(components.size() - 1);
    }

    /**
     * Convert unix timestamp to a duration forward or backward to current system
     * time
     * 
     * @param expires A unix timestamp
     * @return A string stating the duration forward or backward in time.
     */
    public static String expires(long expires) {
        long CurTime = TimeUtil.getUnixTime();
        if (expires == 0)
            return "never expires";
        else if (expires < CurTime)
            return String.format("%s ago", TimeUtil.durationString(expires));
        else
            return String.format("%s from now", TimeUtil.durationString(expires));
    }

    private static HashMap<Character, Long> durationChars = new HashMap<>();
    static {
        durationChars.put('Y', 31536000L);
        durationChars.put('y', 31536000L);
        durationChars.put('M', 2592000L);
        durationChars.put('W', 604800L);
        durationChars.put('w', 604800L);
        durationChars.put('D', 86400L);
        durationChars.put('d', 86400L);
        durationChars.put('H', 3600L);
        durationChars.put('h', 3600L);
        durationChars.put('m', 60L);
        durationChars.put('S', 1L);
        durationChars.put('s', 1L);
    }

    /**
     * Parse a duration string to a length of time in seconds.
     * 
     * @param s the string of characters to convert to a quantity of seconds
     * @return The duration converted to seconds
     */
    public static Optional<Long> duration(String s) {
        var total = 0L;
        var subtotal = 0L;

        for (var i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if ((ch >= '0') && (ch <= '9'))
                subtotal = (subtotal * 10) + (ch - '0');
            else {
                /*
                 Found something thats not a number, find out how much it multiplies the built
                 up number by, multiply the total and reset the built up number.
                 */

                Long multiplier = TimeUtil.durationChars.get(ch);
                if (multiplier == null)
                    return Optional.empty();

                total += subtotal * multiplier;

                // Next subtotal please!
                subtotal = 0;
            }
        }
        return Optional.of(total);
    }

    /**
     * Convert a unix timestamp to a human readable date string
     * 
     * @param t Number of seconds since unix epoch
     * @return A string with the format "EEE, MMM d yyyy HH:mm:ss"
     * @deprecated Use the
     *             {@link com.dumbdogdiner.stickyapi.common.translation.Translation}
     *             functions for converting timestamps to human readable strings.
     *             {@link java.text.SimpleDateFormat}
     */
    @Deprecated
    public static String timeString(long t) {
        return TimeUtil.timeString(new Timestamp(t));
    }

    /**
     * Convert a SQL Timestamp to a human readable date string
     * 
     * @param ts The time as a Timestamp object
     * @return A string with the format "EEE, MMM d yyyy HH:mm:ss"
     * @deprecated Use the
     *             {@link com.dumbdogdiner.stickyapi.common.translation.Translation}
     *             functions for converting timestamps to human readable strings.
     *             {@link java.text.SimpleDateFormat}
     */
    @Deprecated
    public static String timeString(Timestamp ts) {
        if (ts == null)
            return "";
        return sdf.format(ts);
    }

    /**
     * Convert a human-readable duration to a SQL Timestamp object
     * 
     * @param timePeriod A duration string (eg, "2y1w10d40m6s")
     * @return {@link java.sql.Timestamp}
     */
    // I spent 2 hours ensuring this method worked...
    // Turns out, I need to read documentation more, java.sql.Timestamp uses
    // milliseconds and not seconds, I somehow forgot that.
    public static Timestamp toTimestamp(String timePeriod) {
        // Parse ban time.
        if (StringUtil.compareMany(timePeriod, new String[] { "*", "0" }))
            return null;

        // If it's numeric, lets do some extra checks!
        if (NumberUtil.isNumeric(timePeriod)) {
            // Return null if it's greater 12 characters long
            if (timePeriod.length() > 12)
                return null;
            Optional<Long> dur = TimeUtil.duration(timePeriod);
            if (dur.isPresent())
                return new Timestamp((TimeUtil.getUnixTime() + dur.get()) * 1000L)
                        .getTime() >= new java.sql.Timestamp(253402261199L * 1000L).getTime() ? null
                                : new Timestamp((TimeUtil.getUnixTime() + dur.get()) * 1000L);
            // 253402261199 x 1000L is the year 9999 in epoch, this ensures we don't have
            // invalid timestamp exceptions.
        }
        if (!StringUtil.compareMany(timePeriod, new String[] { "*", "0" })) {
            Optional<Long> dur = TimeUtil.duration(timePeriod);
            if (dur.isPresent())
                return new Timestamp((TimeUtil.getUnixTime() + dur.get()) * 1000L)
                        .getTime() >= new java.sql.Timestamp(253402261199L * 1000L).getTime() ? null
                                : new Timestamp((TimeUtil.getUnixTime() + dur.get()) * 1000L);
        }

        return null;
    }

    /**
     * Get the current system time as a Unix timestamp
     * 
     * @return Current number of seconds since Unix Epoch
     */
    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * Get the current system time as a SQL Timestamp object
     * 
     * @return {@link java.sql.Timestamp}
     */
    public static Timestamp now() {
        return new Timestamp(TimeUtil.getUnixTime() * 1000L);
    }
}
