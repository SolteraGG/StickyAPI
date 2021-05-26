/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.jvm;


import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class EncapsulationArgumentTest {

    @Test
    public void testEqualsHashCodeContracts() {
        EqualsVerifier.forClass(EncapsulationArgument.class).verify();
    }
    
}
