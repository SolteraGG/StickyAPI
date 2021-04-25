/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.dumbdogdiner.stickyapi.math.RandomUtil;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class NbtNumberTagTest {
    private static final int REPEAT = 100; // originally 1000, moved to 100 for now
    int i;
    float f;

    @BeforeEach
    public void setUp(){
        i = RandomUtil.randomInt(-256,  4096);
        f = (float) RandomUtil.randomDouble(-256, 8192);
    }

    @RepeatedTest(REPEAT)
    void testFromPrimitive() {
        System.out.println(MessageFormat.format("i={0,number,#.##########}; f={1,number,#.##########}", i, f));
        JsonPrimitive iPrim = new JsonPrimitive(i);
        JsonPrimitive fPrim = new JsonPrimitive(f);
        assertEquals(new NbtNumberTag(i), NbtNumberTag.fromPrimitive(iPrim));
        assertEquals(new NbtNumberTag(f), NbtNumberTag.fromPrimitive(fPrim));
    }

    @RepeatedTest(REPEAT)
    void testAsPrimitive() {
        System.out.println(MessageFormat.format("i={0,number,#.##########}; f={1,number,#.##########}", i, f));
        assertEquals(i, new NbtNumberTag(i).asPrimitive());
        assertEquals(i, NbtNumberTag.fromPrimitive(new JsonPrimitive(i)).asPrimitive());
        assertEquals(f, new NbtNumberTag(f).asPrimitive());
        assertEquals(f, NbtNumberTag.fromPrimitive(new JsonPrimitive(f)).asPrimitive());
    }

    @RepeatedTest(REPEAT)
    void testToJson() {
        System.out.println(MessageFormat.format("i={0,number,#.##########}; f={1,number,#.##########}", i, f));
        JsonPrimitive iPrim = new JsonPrimitive(i);
        JsonPrimitive fPrim = new JsonPrimitive(f);
        assertEquals(iPrim, new NbtNumberTag(i).toJson());
        assertEquals(iPrim, NbtNumberTag.fromPrimitive(iPrim).toJson());
        assertEquals(fPrim, new NbtNumberTag(f).toJson());
        assertEquals(fPrim, NbtNumberTag.fromPrimitive(fPrim).toJson());
    }

    @RepeatedTest(REPEAT)
    void testToNbtString() {
        System.out.println(MessageFormat.format("i={0,number,#.##########}; f={1,number,#.##########}", i, f));
        String iString = new NbtNumberTag(i).toNbtString();
        String negativeIString = new NbtNumberTag(-i).toNbtString();
        String fString = new NbtNumberTag(f).toNbtString();
        String negativeFString = new NbtNumberTag(-f).toNbtString();
        assertEquals(NbtNumberTag.NUMBER_FORMAT.format(i), iString);
        assertEquals(NbtNumberTag.NUMBER_FORMAT.format(f), fString);
        assertEquals(NbtNumberTag.NUMBER_FORMAT.format(-i), negativeIString);
        assertEquals(NbtNumberTag.NUMBER_FORMAT.format(-f), negativeFString);

        assumeTrue(i > -i);
        assertFalse(iString.contains("-"));
        assertFalse(iString.contains("."));
        assertFalse(iString.contains(","));
        assertTrue(negativeIString.contains("-"));
        assertFalse(negativeIString.contains("."));
        assertFalse(negativeIString.contains(","));

        assumeTrue(f != (int) f);
        assumeTrue(f > -f);
        assertTrue(fString.contains("."));
        assertFalse(fString.contains("-"));
        assertFalse(fString.contains(","));
        assertTrue(negativeFString.contains("."));
        assertTrue(negativeFString.contains("-"));
        assertFalse(negativeFString.contains(","));
    }

    @RepeatedTest(REPEAT)
    void testNotEquals() {
        assertNotEquals(new NbtNumberTag(i), NbtNumberTag.fromPrimitive(new JsonPrimitive(i - 1)));
        assertNotEquals(new NbtNumberTag(f), NbtNumberTag.fromPrimitive(new JsonPrimitive(f + 0.1f)));
    }

    @Test
    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    void testEquals() {
        assertEquals(new NbtNumberTag(0), NbtNumberTag.FALSE);
        assertEquals(new NbtNumberTag(0), new NbtBooleanTag(false));
        assertEquals(new NbtNumberTag(1), NbtNumberTag.TRUE);
        assertEquals(new NbtNumberTag(1), new NbtBooleanTag(true));
    }
}
