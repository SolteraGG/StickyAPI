/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import static com.dumbdogdiner.stickyapi_tests_common.TestsCommon.superficialEnumCodeCoverage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dumbdogdiner.stickyapi.common.util.MemoryUtil.Unit;

public class MemoryUtilTest {
    @Test
    public void enumUnit() {
        superficialEnumCodeCoverage(MemoryUtil.Unit.class);
    }

    @Test
    public void testFormatBits() {
        assertEquals(MemoryUtil.formatBits(123, Unit.BITS), 123);
    }

    @Test
    public void testFormatBytes() {
        assertEquals(MemoryUtil.formatBits(8, Unit.BYTES), 1);
    }

    @Test
    public void testFormatKilobytes() {
        assertEquals(MemoryUtil.formatBits(8000, Unit.KILOBYTES), 1);
    }

    @Test
    public void testFormatMegabytes() {
        assertEquals(MemoryUtil.formatBits(8000000, Unit.MEGABYTES), 1);
    }
}
