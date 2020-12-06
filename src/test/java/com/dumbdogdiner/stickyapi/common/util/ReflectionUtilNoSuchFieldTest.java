/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReflectionUtilNoSuchFieldTest {
    private class ExampleClass {
        public ExampleClass() {}
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.out.println("*** Begin BeforeEach ***");
        System.out.println("Setting up PrintStreams...");

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);

        System.out.println("Restored PrintStreams.");
    }

    @Test
    public void testSetProtectedValueNoSuchField() {
        // Same function call regardless of object or class, so only one test.

        ExampleClass instance = new ExampleClass();

        ReflectionUtil.setProtectedValue(instance, "nonexistent_field", "value");

        assertEquals(
                "*** com.dumbdogdiner.stickyapi.common.util.ReflectionUtilNoSuchFieldTest$ExampleClass:java.lang.NoSuchFieldException: nonexistent_field"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testGetProtectedValueClassNoSuchField() {
        Class<?> c = ExampleClass.class;

        ReflectionUtil.getProtectedValue(c, "nonexistent_field");

        assertEquals(
                "*** com.dumbdogdiner.stickyapi.common.util.ReflectionUtilNoSuchFieldTest$ExampleClass:java.lang.NoSuchFieldException: nonexistent_field"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testinvokeProtectedMethodClassNoSuchField() {
        Class<?> c = ExampleClass.class;

        ReflectionUtil.invokeProtectedMethod(c, "nonexistent_field");

        assertEquals(
                "*** com.dumbdogdiner.stickyapi.common.util.ReflectionUtilNoSuchFieldTest$ExampleClass.nonexistent_field(): java.lang.NoSuchMethodException: com.dumbdogdiner.stickyapi.common.util.ReflectionUtilNoSuchFieldTest$ExampleClass.nonexistent_field()"
                        + System.lineSeparator(),
                outContent.toString());
    }

}
