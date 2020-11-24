/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilTest {

    @Test
    public void testSignificantDurationString() {
        String time = TimeUtil.significantDurationString(6000000L);
        assertEquals(time, "1.67/h");
    }

    @Test
    public void testDurationString() {
        String time = TimeUtil.durationString(6000000L);
        assertEquals(time, "1 hour, and 40 minutes");
    }
}
