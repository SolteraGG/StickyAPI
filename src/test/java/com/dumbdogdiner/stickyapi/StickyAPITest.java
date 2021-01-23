/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class StickyAPITest {

    @Test
    public void testGetVersion() {
        String version = StickyAPI.getVersion();
        
        assertNotNull(version);
        assertFalse(version.length() == 0);
    }

    @Test
    public void testGetTimestamp() {
        assertNotNull(StickyAPI.getTimestamp());
    }

    @Test
    public void testGetCommit() {
        assertEquals(40, StickyAPI.getCommit().length());
    }

    @Test
    public void testGetSha() {
        String sha = StickyAPI.getSha();

        assertEquals(7, sha.length());
        assertEquals(StickyAPI.getCommit().substring(0, 7), sha);
    }

    @Test
    public void testGetBranch() {
        String branch = StickyAPI.getBranch();

        assertNotNull(branch);
        assertTrue(branch.length() != 0);
    }

    @Test
    // This test is kinda useless?
    public void testGetIsDirty() {
        assertTrue(StickyAPI.getIsDirty() instanceof Boolean);
    }
}
