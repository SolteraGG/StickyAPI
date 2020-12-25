/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IPUtilTest {

    // IPv4 Tests

    @Test
    public void testCheckValidRanges4() {
        assertTrue(IPUtil.isContainedBy("192.0.2.1", "192.0.2.1/32"));
        assertFalse(IPUtil.isContainedBy("192.0.2.1", "192.0.2.2/32"));
    }

    @Test
    public void testCheckSlashOneRange4() {
        assertTrue(IPUtil.isContainedBy("0.0.0.0", "127.255.255.254/1"));
        assertTrue(IPUtil.isContainedBy("0.0.0.0", "127.255.255.255/1"));

        assertFalse(IPUtil.isContainedBy("0.0.0.0", "128.0.0.1/1"));
    }

    @Test
    public void testCheckSlashZeroRange4() {
        assertTrue(IPUtil.isContainedBy("0.0.0.0", "0.0.0.0/0"));
        assertTrue(IPUtil.isContainedBy("0.0.0.0", "255.255.255.255/0"));
    }

    // IPv6 Tests

    @Test
    public void testCheckValidRanges6Full() {
        assertTrue(IPUtil.isContainedBy("0000:0000:0000:0000:0000:0000:0000:0001",
                "0000:0000:0000:0000:0000:0000:0000:0001/128"));
        assertFalse(IPUtil.isContainedBy("0000:0000:0000:0000:0000:0000:0000:0001",
                "0000:0000:0000:0000:0000:0000:0000:0002/128"));
    }

    @Test
    public void testCheckValidRanges6Simple() {
        assertTrue(IPUtil.isContainedBy("::1", "::1/128"));
        assertFalse(IPUtil.isContainedBy("::1", "::2/128"));
    }

    @Test
    public void testCheckSlashOneRange6() {
        assertTrue(IPUtil.isContainedBy("::1", "7fff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/1"));

        assertFalse(IPUtil.isContainedBy("::1", "8000::/1"));
    }

    @Test
    public void testCheckSlashZeroRange6() {
        assertTrue(IPUtil.isContainedBy("::1", "::1/0"));
        assertTrue(IPUtil.isContainedBy("::1", "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/0"));
    }

    // Other Tests

    @Test
    public void testCheckInvalidArguments() {
        assertFalse(IPUtil.isContainedBy("OwO", "1.1.1.1/32"));
        assertFalse(IPUtil.isContainedBy("1.1.1.1", "OwO/32"));
        assertFalse(IPUtil.isContainedBy("1.1.1.1", "1.1.1.1/OwO"));
    }

    @Test
    public void testValidationCheck4() throws Exception {
        assertFalse(IPUtil.isValid("a.b.c.d"));
        assertFalse(IPUtil.isValid("1234.1234.1234.1234"));
        assertFalse(IPUtil.isValid("999.999.999.999"));
        assertTrue(IPUtil.isValid("1.1.1.1"));
    }

    @Test
    public void testValidationCheck6() throws Exception {
        assertFalse(IPUtil.isValid("gggg:gggg:gggg:gggg:gggg:gggg:gggg:gggg"));
        assertTrue(IPUtil.isValid("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/0"));
    }
}
