/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util;

import static com.dumbdogdiner.stickyapi_tests_common.TestsCommon.superficialEnumCodeCoverage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MemoryUtilTest {
    @Test
    public void enumUnit() {
        superficialEnumCodeCoverage(MemoryUtil.Unit.class);
    }

    @Test
    public void testFormatBits() {
        assertEquals(MemoryUtil.formatBits(123, MemoryUtil.Unit.BITS), 123);
    }

    @Test
    public void testFormatBytes() {
        assertEquals(MemoryUtil.formatBits(8, MemoryUtil.Unit.BYTES), 1);
    }

    @Test
    public void testFormatKilobytes() {
        assertEquals(MemoryUtil.formatBits(8000, MemoryUtil.Unit.KILOBYTES), 1);
    }

    @Test
    public void testFormatMegabytes() {
        assertEquals(MemoryUtil.formatBits(8000000, MemoryUtil.Unit.MEGABYTES), 1);
    }
}
