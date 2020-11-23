/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest {
    @Test
    public void testCreateProgressBarReducedArgs() {
        // Create the progress bar
        String bar = StringUtil.createProgressBar(10, 50);

        // Check size
        // Reduced arg adds brackets, so length should be 12.
        assertEquals(bar.length(), 12);

        // Check occurences of bar (should be 5)
        Pattern pattern = Pattern.compile("[^\u258D]*\u258D");
        Matcher matcher = pattern.matcher(bar);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        assertEquals(count, 5);
    }

    @Test
    public void testCreateProgressBar() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 100, true, true, true);

        // Check size - brackets are added so 12.
        assertEquals(bar.length(), 17, bar);
        // 10x '|'
        // 1x '['
        // 1x ']'
        // 1x ' '
        // 1x '%'
        // 3x percentage | '100'

        // Check occurences of bar (should be 10)
        Pattern pattern = Pattern.compile("[^\\|]*\\|");
        Matcher matcher = pattern.matcher(bar);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        assertEquals(count, 10);
    }

    @Test
    public void testCreateProgressBarNoBrackets() {
        String bar = StringUtil.createProgressBar(10, 50, false, false, false);
        assertEquals(bar.length(), 10);
    }

    @Test
    public void testCreateProgressBarStrictAssertFull() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 100, true, true, true);
        assertEquals(bar, "[||||||||||] 100%");
    }

    @Test
    public void testCreateProgressBarStrictAssertHalf() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 50, true, true, true);
        assertEquals(bar, "[|||||     ] 50%");
    }

    @Test
    public void testCreateProgressBarStrictAssertThird() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 33, true, true, true);
        assertEquals(bar, "[||||      ] 33%");
    }

    @Test
    public void testCreateProgressBarNoBracketsStrictAssertFull() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 100, false, false, false);
        assertEquals(bar, "\u258D\u258D\u258D\u258D\u258D\u258D\u258D\u258D\u258D\u258D");
    }

    @Test
    public void testCreateProgressBarNoBracketsStrictAssertHalf() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 50, false, false, false);
        assertEquals(bar, "\u258D\u258D\u258D\u258D\u258D     ");
    }

    @Test
    public void testCreateProgressBarNoBracketsStrictAssertThird() {
        // Brackets, Monospace, Percentage
        String bar = StringUtil.createProgressBar(10, 33, false, false, false);
        assertEquals(bar, "\u258D\u258D\u258D\u258D      ");
    }

    // capitaliseSentence<keepUpperCase>

    @Test
    public void testCapitaliseSentence() {
        assertEquals(StringUtil.capitaliseSentence("hello world"), "Hello World");
        assertEquals(StringUtil.capitaliseSentence("Hello World"), "Hello World");
        assertEquals(StringUtil.capitaliseSentence("Hello wOrld"), "Hello World");
    }

    @Test
    public void testCapitaliseSentenceKeepUpperCase() {
        assertEquals(StringUtil.capitaliseSentenceKeepUpperCase("hello world"), "Hello World");
        assertEquals(StringUtil.capitaliseSentenceKeepUpperCase("Hello World"), "Hello World");
        assertEquals(StringUtil.capitaliseSentenceKeepUpperCase("Hello wOrld"), "Hello WOrld");
    }

    // censorWord

    @Test
    public void testCensorWord() {
        assertEquals(StringUtil.censorWord("hi"), "**");
    }

    // replaceLeet

    @Test
    public void testReplaceLeetEmptyMsg() {
        assertEquals(StringUtil.replaceLeet(""), "");
    }

    @Test
    public void testReplaceLeet() {
        // From javadoc example.
        assertEquals(StringUtil.replaceLeet("50m3 1337 5p34k h3r3"), "some leet speak here");
    }

    // compareMany

    @Test
    public void testCompareMany() {
        assertTrue(StringUtil.compareMany("hello there", new String[] { "hello there" }));
        assertTrue(StringUtil.compareMany("hello there", new String[] { "Hello there" }));

        assertFalse(StringUtil.compareMany("hello there", new String[] { "goodbye" }));
    }
}
