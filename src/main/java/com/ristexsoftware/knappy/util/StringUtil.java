package com.ristexsoftware.knappy.util;

import java.util.HashMap;
import java.util.Map;

public class StringUtil {

    private static HashMap<String, String> leetReplace = new HashMap<>();

    static {
        leetReplace.put("0", "o");
        leetReplace.put("1", "i");
        leetReplace.put("3", "e");
        leetReplace.put("4", "a");
        leetReplace.put("5", "s");
        leetReplace.put("6", "d");
        leetReplace.put("7", "t");
        leetReplace.put("_", "");
    }


    /**
     * Create a horizontal progress bar, similar to how htop does it.
     * @param size The size of the bar (inside)
     * @param percentage The percentage to fill the bar to
     * @param monospace If false, the bars will be a chatacter with the equivalent amount of pixels as a whitespace character
     * @param includePercentage If true, the percentage will be appended inside of outside of the bar, depending on how much whitespace is available
     * @return A progress bar
     */
    public static String createProgressBar(double size, double percentage, boolean monospace, boolean includePercentage, boolean includeeBrackets) {
        double barCount = ((percentage/100)*size);
        String bar = "";
        for (double i = 0; i < size; i++) {
            if (i < barCount)
                if (!monospace)
                    bar+="▍";
                else
                    bar+="|";
            else
                bar+=" ";
        }
        if (includeeBrackets)
            bar = "[" + bar + "]";
        return bar;
    }


    /**
     * Create a horizontal progress bar, similar to how htop does it.
     * @param size The size of the bar
     * @param percentage The percentage to fill the bar to
     * @return A progress bar
     */
    public static String createProgressBar(double size, double percentage) {
        return createProgressBar(size, percentage, false, false, true);
    }

    /**
     * Capitalise every letter after whitespace<p>
     * Example: "hello world" == "Hello World"
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
     * Replace a word with asterisks.
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
}