/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextUtilTest {
    @Test
    public void testIsCharacterSupportedTrueBasicPlaneUtf16() {
        assertTrue(TextUtil.isCharacterSupported((char) 0x0041));
    }

    @Test
    public void testIsCharacterSupportedTrueBasicPlaneDecimal() {
        assertTrue(TextUtil.isCharacterSupported((char) 65));
    }

    @Test
    public void testIsCharacterSupportedTrueEmojiUtf16() {
        // 0x1F5E1 | "Dagger Knife" Emoji 🗡️
        assertTrue(TextUtil.isCharacterSupported((char) 0x1F5E1));
    }

    @Test
    public void testIsCharacterSupportedFalseEmojiUtf16() {
        // 0x1F98A | "Fox Face" Emoji 🦊
        assertFalse(TextUtil.isCharacterSupported((char) 0x1F98A));
    }

    @Test
    public void testAllSupportedCharsAreSupported() {
        for(char c : TextUtil.getSupportedCharacters()){
            assertTrue(TextUtil.isCharacterSupported(c));
        }
    }

    @Test
    public void testUnsupportedCharsNotSupported() {
        Set<Character> unsupported = IntStream.range(Character.MIN_VALUE, Character.MAX_VALUE)
                .boxed()
                .map(i -> (char) (int) i)
                .collect(Collectors.toSet());
        unsupported.removeAll(TextUtil.getSupportedCharacters());
        for (Character c : unsupported) {
            try {
                int x = TextUtil.getCharacterWidth(c);
                fail(MessageFormat.format("Should have failed, instead got a width of {0} for char {1}", x, c));
            } catch (IllegalArgumentException e) {
                // expected
                assertFalse(TextUtil.isCharacterSupported(c));
            }
        }
    }

    @Test
    void splitLines() {
    }

    @Test
    void prettifyText() {
        Arrays.asList(TextUtil.prettifyText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "al;ksjfuej;iajdf;luwpqo4q394utp98&(*&)(*")).forEach(System.out::println);
    }
}
