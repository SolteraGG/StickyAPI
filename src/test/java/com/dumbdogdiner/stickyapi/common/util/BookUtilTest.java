/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookUtilTest {
    @Test
    public void testIsCharacterSupportedTrueBasicPlaneUtf16() {
        assertTrue(BookUtil.isCharacterSupported((char) 0x0041));
    }

    @Test
    public void testIsCharacterSupportedTrueBasicPlaneDecimal() {
        assertEquals((char) 0x0041, (char) 65); // Sanity check assertation
        assertTrue(BookUtil.isCharacterSupported((char) 65));
    }

    @Test
    public void testIsCharacterSupportedTrueEmojiUtf16() {
        // 0x1F5E1 | "Dagger Knife" Emoji
        assertTrue(BookUtil.isCharacterSupported((char) 0x1F5E1));
    }
    
    @Test
    public void testIsCharacterSupportedFalseEmojiUtf16() {
        // 0x1F98A | "Fox Face" Emoji
        assertFalse(BookUtil.isCharacterSupported((char) 0x1F98A));
    }
    
}
