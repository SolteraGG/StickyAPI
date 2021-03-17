/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util.reflection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReflectionUtilTest {
    private class ExampleClass {

        private String privateString = "default_value";

        protected String protectedString = "default_value";

        private final String privateFinalString = "default_value";

        private static final String privateStaticFinalString = "default_value";

        public ExampleClass() {
        };
    }

    @Test
    public void testProtectedValuePrivate() {
        ExampleClass instance = new ExampleClass();

        assertEquals(instance.privateString, "default_value");

        ReflectionUtil.setProtectedValue(instance, "privateString", "edited_value");

        // assertEquals(ReflectionUtil.getProtectedValue(instance, "privateString"),
        // "edited_value");
        assertEquals(instance.privateString, "edited_value");
    }

    @Test
    public void testProtectedValuePrivateFinal() {
        ExampleClass instance = new ExampleClass();

        assertEquals(instance.privateFinalString, "default_value");

        ReflectionUtil.setProtectedValue(instance, "privateFinalString", "edited_value");

        assertEquals(ReflectionUtil.getProtectedValue(instance, "privateFinalString"), "edited_value");
        // Doesn't work here - have to use ReflectionUtil's get!
        // assertEquals(instance.privateFinalString, "edited_value");
    }

    @Test
    public void testProtectedValuePrivateStaticFinal() {
        Class<?> c = ExampleClass.class;

        assertEquals(ExampleClass.privateStaticFinalString, "default_value");

        ReflectionUtil.setProtectedValue(c, "privateStaticFinalString", "edited_value");

        assertEquals(ReflectionUtil.getProtectedValue(c, "privateStaticFinalString"), "edited_value");

        assertEquals(ReflectionUtil.getProtectedValue(c, "privateStaticFinalString"), "edited_value");
        // Doesn't work here - have to use ReflectionUtil's get!
        // assertEquals(c.privateStaticFinalString, "edited_value");
    }

    @Test
    public void testProtectedValueProtected() {
        ExampleClass instance = new ExampleClass();

        assertEquals(instance.protectedString, "default_value");

        ReflectionUtil.setProtectedValue(instance, "protectedString", "edited_value");

        assertEquals(ReflectionUtil.getProtectedValue((Object) instance, "protectedString"), "edited_value");
        assertEquals(instance.protectedString, "edited_value");
    }

    @Test
    public void testGetProtectedValueObjectNoSuchField() {
        ExampleClass instance = new ExampleClass();

        Object o = ReflectionUtil.getProtectedValue((Object) instance, "nonexistent_field");

        assertNull(o);
    }
}
