/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class ReflectionUtilInvokeProtectedMethodClassTest {
    // Declare example private static methods here.
    private static void privateStaticMethod() {}
    private static void privateStaticMethodArgsString(String a) {}

    private static void privateStaticMethodArgsInt(int a) {}

    // Both of these variables are called via ReflectionUtil so will otherwise show as unused.

    @SuppressWarnings("unused")
    private void privateMethod() {
        privateMethodHasRun = true;
    }
    
    @SuppressWarnings("unused")
    private boolean privateMethodHasRun = false;

    @Test
    public void testInvokeProtectedMethodClass() {
        try (MockedStatic<ReflectionUtilInvokeProtectedMethodClassTest> mocked = mockStatic(ReflectionUtilInvokeProtectedMethodClassTest.class)) {
            mocked.when(ReflectionUtilInvokeProtectedMethodClassTest::privateStaticMethod).thenCallRealMethod();

            ReflectionUtil.invokeProtectedMethod((Class<?>) ReflectionUtilInvokeProtectedMethodClassTest.class, "privateStaticMethod");

            mocked.verify(ReflectionUtilInvokeProtectedMethodClassTest::privateStaticMethod);
        }
    }

    @Test
    public void testInvokeProtectedMethodClassArgsString() {
        try (MockedStatic<ReflectionUtilInvokeProtectedMethodClassTest> mocked = mockStatic(ReflectionUtilInvokeProtectedMethodClassTest.class)) {
            mocked.when(() -> ReflectionUtilInvokeProtectedMethodClassTest.privateStaticMethodArgsString(anyString())).thenCallRealMethod();
            
            ReflectionUtil.invokeProtectedMethod((Class<?>) ReflectionUtilInvokeProtectedMethodClassTest.class,"privateStaticMethodArgsString", (Object)(String)"A");

            mocked.verify(() -> ReflectionUtilInvokeProtectedMethodClassTest.privateStaticMethodArgsString(anyString()));
        }
    }

    @Test
    public void testInvokeProtectedMethodClassArgsInteger() {
        try (MockedStatic<ReflectionUtilInvokeProtectedMethodClassTest> mocked = mockStatic(
                ReflectionUtilInvokeProtectedMethodClassTest.class)) {
            mocked.when(() -> ReflectionUtilInvokeProtectedMethodClassTest.privateStaticMethodArgsInt(anyInt()))
                    .thenCallRealMethod();

            ReflectionUtil.invokeProtectedMethod((Class<?>) ReflectionUtilInvokeProtectedMethodClassTest.class,
                    "privateStaticMethodArgsInt", (int) 1);

            mocked.verify(() -> ReflectionUtilInvokeProtectedMethodClassTest.privateStaticMethodArgsInt(anyInt()));
        }
    }

    @Test
    public void testInvokeProtectedMethodObject() {
        ReflectionUtilInvokeProtectedMethodClassTest c = new ReflectionUtilInvokeProtectedMethodClassTest();
        
        ReflectionUtil.invokeProtectedMethod(c, "privateMethod");

        assertTrue((boolean) ReflectionUtil.getProtectedValue(c, "privateMethodHasRun")); // set to true while running the method
    }
}
