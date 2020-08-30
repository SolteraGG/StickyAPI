package com.ristexsoftware.knappy.util;

public class StringUtil {

    /**
     * Create a horizontal percentage bar, similar to how htop does it.
     * @param size The size of the bar (inside)
     * @param percentage The percentage to fill the bar to
     * @param monospace If false, the bars will be a chatacter with the equivalent amount of pixels as a whitespace character
     * @param includePercentage If true, the percentage will be appended inside of outside of the bar, depending on how much whitespace is available
     * @return
     */
    public static String createBar(double size, double percentage, boolean monospace, boolean includePercentage) {
        double barCount = ((percentage/100)*size);
        String bar = "[";
        for (double i = 0; i < size; i++) {
            if (i < barCount)
                if (!monospace)
                    bar+="▍";
                else
                    bar+="|";
            else 
                bar+=" ";
        }
        bar+="]";
        return bar;
    }

    public static String createBar(double size, double percentage) {
        return createBar(size, percentage, false, false);
    }
}