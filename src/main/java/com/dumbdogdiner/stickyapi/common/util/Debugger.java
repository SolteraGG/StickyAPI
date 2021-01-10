/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import lombok.Setter;
import lombok.Getter;

/**
 * Utility class for debugging.
 */
public class Debugger {
    /**
     * The logger to use when printing.
     */
    @Setter
    private static Logger logger = Logger.getLogger("DEBUG");

    @Setter
    @Getter
    private static boolean enabled = false;

    /**
     * The time at which this debugger instance began logging.
     * 
     * @return {@link Long}
     */
    @Getter
    private Long startTime = System.nanoTime();

    /**
     * The current increment count of the log - equivalent to how many times
     * <code>Debugger.print(Object message)</code> has been called.
     * 
     * @return {@link Integer}
     */
    @Getter
    private int logCount = 0;

    private final Class<?> clazz;

    private Random r = new Random();
    private final String ALPHABET = "3569abcde";
    private final String COLOR = "\u00A7" + ALPHABET.charAt(r.nextInt(ALPHABET.length()));

    /**
     * Create a debugger instance that references the provided class. Allows for
     * per-class debugging.
     * 
     * @param clazz to reference.
     */
    public Debugger(@NotNull Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Print a debug message.
     * 
     * @param object to print.
     * @param args   to format the message with
     */
    public void print(@Nullable Object object, @Nullable Object... args) {
        if (enabled) {
            logger.info(String.format(COLOR + "[" + ++logCount + " | " + clazz.getSimpleName() + ".class: "
                    + dddGetThisLineOfWhereverThisThingIsCalleduwu() + "] \u00A7r" + object + " | " // \u00A7 = section symbol
                    + ((System.nanoTime() - startTime) / 1e3) + "μ", args));
        }
    }

    /**
     * Reset this debugger instance, setting <code>startTime</code> to the current
     * time, and <code>logCount</code> to 0.
     * 
     * @return {@link Debugger}
     */
    public Debugger reset() {
        startTime = System.nanoTime();
        logCount = 0;
        return this;
    }

    /**
     * This methods name is ridiculous on purpose to prevent any other method names
     * in the stack trace from potentially matching this one.
     * 
     * The line number of the code that called the method that called this
     * method(Should only be called by getLineNumber()).
     * 
     * @return {@link java.lang.Integer}
     * @author Brian_Entei
     */
    // Thanks Brian! https://stackoverflow.com/a/26410435/11988998
    private int dddGetThisLineOfWhereverThisThingIsCalleduwu() {
        boolean thisOne = false;
        int thisOneCountDown = 1;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            String methodName = element.getMethodName();
            int lineNum = element.getLineNumber();
            if (thisOne && (thisOneCountDown == 0)) {
                return lineNum;
            } else if (thisOne) {
                thisOneCountDown--;
            }
            if (methodName.equals("dddGetThisLineOfWhereverThisThingIsCalleduwu")) {
                thisOne = true;
            }
        }
        return -1;
    }
}
