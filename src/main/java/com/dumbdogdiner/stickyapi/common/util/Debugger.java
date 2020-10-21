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

    private final Class<?> debuggedClass;

    private final Random r = new Random();
    private final String ALPHABET = "3569abcde";
    private final String COLOR = "\u00A7" + ALPHABET.charAt(r.nextInt(ALPHABET.length()));
    
    /**
     * Create a debugger instance that references the provided class. Allows for per-class debugging.
     * @param debuggedClass to reference.
     */
    public Debugger(Class<?> debuggedClass) {
        this.debuggedClass = debuggedClass;
    }

    /**
     * Print a debug message.
     * @param object to print.
     */
    public void print(Object object, Object... args) {
        if (enabled) {
            int result = -1;
            boolean thisOne = false;
            int thisOneCountDown = 1;
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for(StackTraceElement element : elements) {
                String methodName = element.getMethodName();
                String methodClassName = element.getClassName();
                int lineNum = element.getLineNumber();
                if(thisOne && (thisOneCountDown == 0)) {
                    result = lineNum;
                    break;
                } else if(thisOne) {
                    thisOneCountDown--;
                }
                if(methodName.equals("print") && methodClassName.equals(getClass().getName())) {
                    thisOne = true;
                }
            }
            logger.info(String.format(COLOR + "[" + (++logCount) + " | " + debuggedClass.getSimpleName()
            + ".class: "+ result +"] \u00A7r" + object + " | " + ((System.nanoTime() - startTime) / 1e3) + "\u00B5", args));
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

}
