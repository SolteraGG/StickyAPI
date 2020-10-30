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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Operations on {@link java.lang.String}
 */
public final class StringUtil {
    private StringUtil() {}

    private static HashMap<String, String> leetReplace = new HashMap<>();

    // createProgressBar - format double percentage to no decimal places to avoid it showing as '100.0000%' or something
    private static DecimalFormat percentageFormatter = new DecimalFormat("#");

    static {
        leetReplace.put("0", "o");
        leetReplace.put("1", "l");
        leetReplace.put("3", "e");
        leetReplace.put("4", "a");
        leetReplace.put("5", "s");
        leetReplace.put("6", "d");
        leetReplace.put("7", "t");
        leetReplace.put("_", "");
    }

    /**
     * Create a horizontal progress bar, similar to how htop does it.
     * 
     * @param size              The size of the bar (inside)
     * @param percentage        The percentage to fill the bar to
     * @param monospace         If false, the bars will be a chatacter with the
     *                          equivalent amount of pixels as a whitespace
     *                          character
     * @param includePercentage If true, the percentage will be appended inside of
     *                          outside of the bar, depending on how much whitespace
     *                          is available
     * @param includeBrackets   If true, the progress bar will return wrapped in
     *                          snuggly brackets
     * @return A progress bar
     */
    public static String createProgressBar(double size, double percentage, boolean monospace, boolean includePercentage,
            boolean includeBrackets) {
        double barCount = ((percentage / 100) * size);
        String bar = "";
        for (double i = 0; i < size; i++) {
            if (i < barCount)
                if (!monospace)
                    bar += "\u258D"; // ...
                else
                    bar += "|";
            else
                bar += " ";
        }
        if (includeBrackets)
            // double up %s to escape them
            bar = includePercentage ? String.format("[%s] %s%%", bar, 
                    percentageFormatter.format(percentage)) : "[" + bar + "]";
        return bar;
    }

    /**
     * Create a horizontal progress bar, similar to how htop does it.
     * 
     * @param size       The size of the bar
     * @param percentage The percentage to fill the bar to
     * @return A progress bar
     */
    public static String createProgressBar(double size, double percentage) {
        return createProgressBar(size, percentage, false, false, true);
    }

    /**
     * Capitalise every letter after whitespace.
     * <p>
     * This will also lowercase any uppercase characters.
     * <p>
     * Example: "hello world" == "Hello World"
     * <p>
     * Example: "HELLO WORLD" == "Hello World"
     * 
     * @see Alternate (keeping uppercase): {@link #capitaliseSentenceKeepUpperCase(String)}
     * @param string The string to capitalise
     * @return A message with capital letters after every whitespace
     */
    public static String capitaliseSentence(String string) {
        StringBuilder sb = new StringBuilder();
        boolean cnl = true;
        for (char c : string.toCharArray()) {
            if (cnl && Character.isLetter(c)) {
                sb.append(Character.toUpperCase(c));
                cnl = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
            if (Character.isWhitespace(c)) {
                cnl = true;
            }
        }
        return sb.toString();
    }

    /**
     * Capitalise every letter after whitespace.
     * <p>
     * Will keep uppercase letters uppercase.
     * <p>
     * Example: "hello world" == "Hello World"
     * <p>
     * Example: "hello WORLD" == "Hello WORLD"
     * 
     * @since 2.0
     * @see Alternate (not keeping uppercase): {@link #capitaliseSentence(String)}
     * @param string The string to capitalise
     * @return A message with capital letters after every whitespace
     */
    public static String capitaliseSentenceKeepUpperCase(String string) {
        StringBuilder sb = new StringBuilder();
        boolean cnl = true;
        for (char c : string.toCharArray()) {
            if (cnl && Character.isLetter(c)) {
                sb.append(Character.toUpperCase(c));
                cnl = false;
            } else {
                sb.append(c);
            }
            if (Character.isWhitespace(c)) {
                cnl = true;
            }
        }
        return sb.toString();
    }

    /**
     * Replace a word with asterisks.
     * 
     * @param word The word to censor
     * @return The censored word
     */
    public static String censorWord(String word) {
        StringBuilder asterisks = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            asterisks.append("*");
        }

        return asterisks.toString();
    }

    /**
     * Filter "Leet Speak" out of a message
     * <p>
     * Example:
     * 
     * <pre>
     * Translation.replaceLeet("50m3 1337 5p34k h3r3") = "some leet speak here"
     * </pre>
     * 
     * @param message The message to filter
     * @return The filtered message
     */
    public static String replaceLeet(String message) {
        if (message.trim().isEmpty())
            return message;

        for (Map.Entry<String, String> entry : leetReplace.entrySet())
            message = message.replaceAll(entry.getKey(), entry.getValue());

        return message;
    }

    /**
     * Check if many strings equal a single comparison string
     * 
     * @param haystack the string to compare to
     * @param needles  things that may match the comparison string
     * @return Whether something matches.
     */
    public static boolean compareMany(String haystack, String[] needles) {
        for (String needle : needles) {
            if (haystack.equalsIgnoreCase(needle))
                return true;
        }

        return false;
    }

}