/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.command;

import com.dumbdogdiner.stickyapi.Enums;
import org.junit.jupiter.api.Test;

public class ExitCodeTest {

    @Test
    public void enumExitCode() {
        Enums.superficialEnumCodeCoverage(ExitCode.class);
    }
}
