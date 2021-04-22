/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO Use JGit to verify

public class BuildInfoTest {
    @Test
    public void debugPrintAll() {
        System.out.printf("Version\t %s", BuildInfo.getVersion());
        System.out.printf("\nTimestamp\t %s", BuildInfo.getTimestamp());
        System.out.printf("\nUnix Timestamp\t %d", BuildInfo.getBuiltAt().getEpochSecond());
        System.out.printf("\nCommit\t %s", BuildInfo.getCommit());
        System.out.printf("\nBranch\t %s", BuildInfo.getBranch());
        System.out.printf("\nDirty ws?\t %b", BuildInfo.isDirty());
    }


    @Test
    void getVersion() {
        assertEquals(BuildInfo.getVersion().getMajor(), BuildInfo.getMajor());
        assertEquals(BuildInfo.getVersion().getMinor(), BuildInfo.getMinor());
        assertEquals(BuildInfo.getVersion().getMajor(), BuildInfo.getMajor());
    }

    @Test
    public void testGetVersion() {
        String version = BuildInfo.getVersion().toString();

        assertNotNull(version);
        assertFalse(version.length() == 0);
    }

    @Test
    public void testGetCommit() {
        assertEquals(40, BuildInfo.getCommit().length());
    }

    @Test
    public void testGetBranch() {
        String branch = BuildInfo.getBranch();

        assertNotNull(branch);
        assertTrue(branch.length() != 0);
    }
}
