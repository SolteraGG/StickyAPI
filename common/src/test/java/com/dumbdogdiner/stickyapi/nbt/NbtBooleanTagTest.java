/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.nbt;

import com.google.gson.JsonPrimitive;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static org.junit.jupiter.api.Assertions.*;

@RunWith(Parameterized.class)
class NbtBooleanTagTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testFromPrimitive(boolean b) {
        assertEquals(new NbtBooleanTag(b), NbtBooleanTag.fromPrimitive(new JsonPrimitive(b)));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testAsPrimitive(boolean b) {
        assertEquals(b, new NbtBooleanTag(b).asPrimitive());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testToJson(boolean b) {
        JsonPrimitive primitive = new JsonPrimitive(b);
        assertEquals(primitive, new NbtBooleanTag(b).toJson());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testToNbtString(boolean b) {
        int i = b ? 1 : 0;
        assertEquals(Integer.toString(i), new NbtBooleanTag(b).toNbtString());
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testEquals(boolean b) {
        int i = b ? 1 : 0;
        assertEquals(new NbtNumberTag(i), new NbtBooleanTag(b));
    }
}