/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A class for abusing Java and using reflection to unprotect methods and
 * constructors, use this with a lot of care and try not to break things!
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public final class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static void setProtectedValue(@NotNull Object o, @NotNull String field, Object newValue) {
        setProtectedValue(o.getClass(), o, field, newValue);
    }

    public static void setProtectedValue(@NotNull Class<?> c, @NotNull String field, Object newValue) {
        setProtectedValue(c, null, field, newValue);
    }

    public static void setProtectedValue(@NotNull Class<?> c, Object o, @NotNull String field, Object newValue) {
        try {
            @NotNull Field f = c.getDeclaredField(field);
            f.setAccessible(true);

            if (Modifier.isFinal(f.getModifiers())) {

                // Before we do this, attempt to disabled the reflective access warnings if they aren't already disabled.
                UnsafeUtil.tryDisableIllegalReflectiveAccessWarning();

                FieldUtil.makeNonFinal(f); // Hacky method to allow this to still work in java 12+

                // TODO: Is this check still needed?
                if (Modifier.isFinal(f.getModifiers()))
                    throw new IllegalAccessException("Cannot set field as non-final. Is this assigned final?");
            }

            f.set(o, newValue);
        } catch (@NotNull NoSuchFieldException | IllegalAccessException ex) {
            System.out.println("*** " + c.getName() + ":" + ex);
        }
    }

    public static <T> @Nullable T getProtectedValue(@NotNull Object obj, String fieldName) {
        try {
            Class<?> c = obj.getClass();
            while (c != Object.class) {
                Field[] fields = c.getDeclaredFields();
                for (@NotNull Field f : fields) {
                    if (f.getName() == fieldName) {
                        f.setAccessible(true);
                        return (T) f.get(obj);
                    }
                }
                c = c.getSuperclass();
            }
            // System.out.println("*** " + obj.getClass().getName() + ":No such field");
            return null;
        } catch (Exception ex) {
            // System.out.println("*** " + obj.getClass().getName() + ":" + ex);
            return null;
        }
    }

    public static <T> @Nullable T getProtectedValue(@NotNull Class<?> c, @NotNull String field) {
        try {
            @NotNull Field f = c.getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(c);
        } catch (Exception ex) {
            System.out.println("*** " + c.getName() + ":" + ex);
            return null;
        }
    }

    public static @Nullable Object invokeProtectedMethod(@NotNull Class<?> c, @NotNull String method, Object... args) {
        return invokeProtectedMethod(c, null, method, args);
    }

    public static @Nullable Object invokeProtectedMethod(@NotNull Object o, @NotNull String method, Object... args) {
        return invokeProtectedMethod(o.getClass(), o, method, args);
    }

    public static @Nullable Constructor<?> getProtectedConstructor(@NotNull Class<?> clazz, Class<?>... params) {
        Constructor<?> c;

        try {
            c = clazz.getDeclaredConstructor(params);
        } catch (@NotNull NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }

        if (c == null)
            return null;

        int m = c.getModifiers();
        if (Modifier.isProtected(m))
            c.setAccessible(true);

        return c;
    }

    public static @Nullable Object invokeProtectedMethod(@NotNull Class<?> c, Object o, @NotNull String method, Object @NotNull ... args) {
        try {
            Class<?> @NotNull [] pTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Integer)
                    pTypes[i] = int.class;
                else
                    pTypes[i] = args[i].getClass();
            }

            @NotNull Method m = c.getDeclaredMethod(method, pTypes);
            m.setAccessible(true);
            return m.invoke(o, args);
        } catch (Exception ex) {
            System.out.println("*** " + c.getName() + "." + method + "(): " + ex);
            return null;
        }
    }
}
