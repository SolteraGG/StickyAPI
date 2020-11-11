/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.particle;

import static com.dumbdogdiner.stickyapi_tests_common.TestsCommon.superficialEnumCodeCoverage;

import org.junit.jupiter.api.Test;

public class OrientationTest {
    @Test
    public void enumOrientation() {
        superficialEnumCodeCoverage(Orientation.class);
    }
}
