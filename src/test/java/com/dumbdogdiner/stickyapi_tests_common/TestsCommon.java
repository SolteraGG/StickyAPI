/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi_tests_common;

public class TestsCommon {
    // Enums are counted in code coverage for some reason - so this function just invlokes valueOf on all enums it can find in a enum class.
    // https://stackoverflow.com/questions/4512358/emma-coverage-on-enum-types/4548912
    public static void superficialEnumCodeCoverage(Class<? extends Enum<?>> enumClass) {
        try {
            for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
                enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
