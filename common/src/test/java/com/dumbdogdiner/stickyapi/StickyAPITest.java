/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StickyAPITest {

    @Test
    public void debugPrintAll() {
        System.out.printf("Version\t %s", StickyAPI.getVersion());
        System.out.printf("\nTimestamp\t %s", BuildInfo.getTimestamp().toString());
        System.out.printf("\nCommit\t %s", StickyAPI.getCommit());
        System.out.printf("\nBranch\t %s", StickyAPI.getBranch());
        System.out.printf("\nDirty ws?\t %b", StickyAPI.isDirty());
    }

    @Test
    public void testGetVersion() {
        String version = StickyAPI.getVersion();
        
        assertNotNull(version);
        assertFalse(version.length() == 0);
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
}
