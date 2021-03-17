/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.command;

import static com.dumbdogdiner.stickyapi_tests_common.TestsCommon.superficialEnumCodeCoverage;

import org.junit.jupiter.api.Test;

public class ExitCodeTest {
    @Test
    public void enumExitCode() {
        superficialEnumCodeCoverage(ExitCode.class);
    }
}
