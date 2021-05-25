/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.jvm;

import java.util.List;

import com.dumbdogdiner.stickyapi.common.util.jvm.EncapsulationArgument.EncapsulationType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgumentUtilTest {

    private void checkArgument(
            EncapsulationArgument arg, EncapsulationType type,
            String sourceModule, String sourcePackage,
            String targetModule) {
        assertEquals(type, arg.getType());
        assertEquals(sourceModule, arg.getSourceModule());
        assertEquals(sourcePackage, arg.getSourcePackage());
        assertEquals(targetModule, arg.getTargetModule());
    }
    @Test
    public void testGetAddOpens() {
        List<EncapsulationArgument> arguments = ArgumentUtil.getAddOpens();

        // Currently common tests are started with 3 add opens:
        // --add-opens=java.base/java.lang.reflect=ALL-UNNAMED, (common/build.gradle)
        // --add-opens=java.base/java.util=ALL-UNNAMED, (unknown)
        // --add-opens=java.base/java.lang=ALL-UNNAMED (unknown)

        assertEquals(3, arguments.size());

        // Check #1
        checkArgument(
            arguments.get(0),
            EncapsulationType.ADD_OPENS,
            "java.base",
            "java.lang.reflect",
            "ALL-UNNAMED"
        );

        // Check #2
        checkArgument(
            arguments.get(1),
            EncapsulationType.ADD_OPENS,
            "java.base",
            "java.util",
            "ALL-UNNAMED"
        );

        // Check #3
        checkArgument(
            arguments.get(2),
            EncapsulationType.ADD_OPENS,
            "java.base",
            "java.lang",
            "ALL-UNNAMED"
        );

    }
}
