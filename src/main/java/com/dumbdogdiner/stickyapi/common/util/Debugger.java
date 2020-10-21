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

import java.util.Random;
import java.util.logging.Logger;

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
     */
    @Getter
    private Long startTime = System.nanoTime();

    /**
     * The current increment count of the log - equivalent to how many times <code>Debugger.print(Object message)</code>
     * has been called.
     */
    @Getter
    private int logCount = 0;

    private Class<?> clazz;

    private Random r = new Random();
    private final String ALPHABET = "3569abcde";
    private final String COLOR = "\u00A7" + ALPHABET.charAt(r.nextInt(ALPHABET.length()));
    
    /**
     * Create a debugger instance that references the provided class. Allows for per-class debugging.
     * @param clazz to reference.
     */
    public Debugger(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Print a debug message.
     * @param object to print.
     */
    public void print(Object object, Object... args) {
        if (enabled) {
            logger.info(String.format(COLOR + "[" + ++logCount + " | " + clazz.getSimpleName()
            + ".class: "+Fi5vGG6kBJbVhpjH3p8PaubeS2Mdtps()+"] \u00A7r" + object + " | " + ((System.nanoTime() - startTime) / 1e3) + "\u00B5", args));
        }
    }

    /**
     * Reset this debugger instance, setting <code>startTime</code> to the current time, and <code>logCount</code> to 0.
     */
    public Debugger reset() {
        startTime = System.nanoTime();
        logCount = 0;
        return this;
    }

    /** 
     * This methods name is ridiculous on purpose to prevent any other method
     * names in the stack trace from potentially matching this one.
     * 
     * The line number of the code that called the method that called
     * this method(Should only be called by getLineNumber()).
     * 
     * @return {@link java.lang.Integer}
     * @author Brian_Entei
     */
    // Thanks Brian! https://stackoverflow.com/a/26410435/11988998
    private int Fi5vGG6kBJbVhpjH3p8PaubeS2Mdtps() {
        boolean thisOne = false;
        int thisOneCountDown = 1;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for(StackTraceElement element : elements) {
            String methodName = element.getMethodName();
            int lineNum = element.getLineNumber();
            if(thisOne && (thisOneCountDown == 0)) {
                return lineNum;
            } else if(thisOne) {
                thisOneCountDown--;
            }
            if(methodName.equals("Fi5vGG6kBJbVhpjH3p8PaubeS2Mdtps")) {
                thisOne = true;
            }
        }
        return -1;
    }
}
