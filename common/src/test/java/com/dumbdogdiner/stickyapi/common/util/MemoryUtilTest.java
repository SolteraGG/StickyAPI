/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dumbdogdiner.stickyapi.Enums;
import com.dumbdogdiner.stickyapi.common.util.MemoryUtil.Unit;
import org.junit.jupiter.api.Test;

public class MemoryUtilTest {

    @Test
    public void enumUnit() {
        Enums.superficialEnumCodeCoverage(MemoryUtil.Unit.class);
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
