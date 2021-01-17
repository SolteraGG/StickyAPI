/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.reflection;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.misc.Unsafe;

/**
 * A utility class for the Java internal {@link sun.misc.Unsafe Unsafe} class -
 * a class with low-level mechanisms designed only to be used by the core Java
 * library.
 * 
 * @since 2.0
 */
public class UnsafeUtil {
    private UnsafeUtil() {
    }

    /**
     * Get an instance of the Unsafe class.
     * 
     * This method uses reflection to avoid a SecurityException.
     * 
     * @return {@link sun.misc.Unsafe Unsafe} - an instance of the Unsafe class.
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Unsafe getUnsafe()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        return unsafe;
    }

    private static boolean illegalReflectiveAccessIsDisabled = false;

    /**
     * A method that attempts to disable the JVM warning for Illegal Reflective
     * Access.
     */
    public static void tryDisableIllegalReflectiveAccessWarning() {
        Logger l = Logger.getGlobal();
        if (illegalReflectiveAccessIsDisabled)
            return;
        l.log(Level.WARNING,
                "*** StickyAPI Warning: Attempting to disable the Illegal Reflective Access JVM Warning! ***");
        System.out.println();

        try {
            Unsafe u = getUnsafe();

            Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);

            l.log(Level.WARNING, "*** StickyAPI Warning: Illegal Reflective Access Warnings disabled! ***");
            illegalReflectiveAccessIsDisabled = true; // prevent this function from fully executing again.
        } catch (Exception e) {
            l.log(Level.WARNING,
                    "*** StickyAPI Warning: Illegal Reflective Access Warnings could not be disabled! ***");
        }
    }
}
