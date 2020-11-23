/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LuhnTest {
    
    @Test
    public void luhnCheckValid() {
        assertTrue(Luhn.luhnCheck("402007136885364"));
    }

    @Test
    public void luhnCheckInvalid() {
        assertFalse(Luhn.luhnCheck("123456789012345"));
    }

    @Test
    public void luhnCheckNull() {
        assertFalse(Luhn.luhnCheck(null));
    }

    @Test
    public void luhnCheckEmptyStr() {
        assertFalse(Luhn.luhnCheck(""));
    }

    @Test
    public void luhnCheckOneCharStrLetter() {
        assertFalse(Luhn.luhnCheck("A"));
    }

    @Test
    public void luhnCheckOneCharStrNumber() {
        assertFalse(Luhn.luhnCheck("1"));
    }

    //TODO: Test to see if it can handle supplementary characters?

    @Test
    public void calculateCheckDigitNull() {
        assertNull(Luhn.calculateCheckDigit(null));
    }

    @Test
    public void calculateCheckDigitEmptyStr() {
        assertTrue(Luhn.calculateCheckDigit("").equalsIgnoreCase("0"));
    }
}
