package com.ristexsoftware.knappy.util;

public class StringUtil {

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
}