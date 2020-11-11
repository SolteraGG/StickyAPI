/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Provides a very crude method of measuring the memory consumption of Java objects. This
 * can never be 100% accurate, since the JVM creates some overhead with each new object, but
 * can be useful to measure the estimated size of an object.
 */
public final class MemoryUtil {
    private MemoryUtil() {}

    public enum Unit {
        BITS, BYTES, KILOBYTES, MEGABYTES
    }

    /**
     * Get the size of an object, specifying in what units the method should return.
     */
    public static Double getSizeOf(Object object, Unit units) {
        int bits = getSizeOf(object);
        return formatBits(bits, units);
    }

    /**
     * Format a number of bits into their unit equivalent.
     */
    public static Double formatBits(int bits, Unit units) {
        switch (units) {
            case BITS:
                return (double) bits;
            case BYTES:
                return (double) bits / 8;
            case KILOBYTES:
                return (double) bits / 8 / 1000;
            case MEGABYTES:
                return (double) bits / 8 / 1000 / 1000;
            default: 
                return 0.0;
        }  
    }

    /**
     * Get the approximate size of the given object.
     */
    public static int getSizeOf(Object object) {
        if (object == null) {
            return 0;
        }

        Class<?> clazz = object.getClass();
        int accumulator = 0;

        int size = getSizeOfBuiltin(object);
        if (size != 0) {
            return size;
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            accumulator += getFieldSize(object, field);
        }

        return accumulator;
    }

    /**
     * Get the size of a field on a given object.
     */
    private static int getFieldSize(Object object, Field field) {
        try {
            // Java complains about illegal reflective access... Too bad!
            return getSizeOf(ReflectionUtil.getProtectedValue(object, field.getName()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getSizeOfBuiltin(Object object) {
        // We can't use a switch statement on an object, so we need to use an if-elseif chain.
        // It may be Yandre Dev - like, but at least instanceof should be a little neater
        if (object instanceof String)
            return ((String) (object)).length() * 8;
        else if(object instanceof Boolean)
            return 1;
        else if(object instanceof Byte)
            return 8;
        else if(object instanceof Short || object instanceof Character)
            return 16;
        else if(object instanceof Long || object instanceof Double)
            return 64;
        else
            // potential for stack overflow if a field is circular! (maybe, but I don't see how this could happen)
            return 0;
    }
}
