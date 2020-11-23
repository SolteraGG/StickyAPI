/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

import com.dumbdogdiner.stickyapi.common.ServerVersion.ServerType;

public class ServerVersionTest {

    @Test
    public void testGetServerTypeBukkit() {
        try (MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)) {
            // Set stub return values when methods are called
            mocked.when(ServerVersion::isBukkit).thenReturn(true);
            mocked.when(ServerVersion::isPaper).thenReturn(false);
            mocked.when(ServerVersion::isSpigot).thenReturn(false);
            mocked.when(ServerVersion::getServerType).thenCallRealMethod(); // Bypass mocking

            assertTrue(ServerVersion.getServerType().equals(ServerType.BUKKIT));

            mocked.verify(ServerVersion::isBukkit);
            mocked.verify(ServerVersion::isPaper);
            mocked.verify(ServerVersion::isSpigot);
            mocked.verify(ServerVersion::getServerType);
        }
    }
    
    @Test
    public void testGetServerTypeSpigot() {
        try (MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)) {
            // Set stub return values when methods are called
            mocked.when(ServerVersion::isBukkit).thenReturn(true);
            mocked.when(ServerVersion::isPaper).thenReturn(false);
            mocked.when(ServerVersion::isSpigot).thenReturn(true);
            mocked.when(ServerVersion::getServerType).thenCallRealMethod(); // Bypass mocking

            assertTrue(ServerVersion.getServerType().equals(ServerType.SPIGOT));

            mocked.verify(ServerVersion::isBukkit);
            mocked.verify(ServerVersion::isPaper);
            mocked.verify(ServerVersion::isSpigot);
            mocked.verify(ServerVersion::getServerType);
        }
    }

    @Test
    public void testGetServerTypePaper() {
        try (MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)) {
            // Set stub return values when methods are called
            mocked.when(ServerVersion::isBukkit).thenReturn(true);
            mocked.when(ServerVersion::isPaper).thenReturn(true);
            //mocked.when(ServerVersion::isSpigot).thenReturn(false);
            mocked.when(ServerVersion::getServerType).thenCallRealMethod(); // Bypass mocking

            assertTrue(ServerVersion.getServerType().equals(ServerType.PAPER));

            // isSpigot not called as function returns after paper returns true
            mocked.verify(ServerVersion::isBukkit);
            mocked.verify(ServerVersion::isPaper);
            mocked.verify(ServerVersion::getServerType);
        }
    }
    
    @Test
    public void testGetServerTypeWaterfall() {
        try (MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)) {
            // Set stub return values when methods are called
            mocked.when(ServerVersion::isBukkit).thenReturn(false);
            mocked.when(ServerVersion::isWaterfall).thenReturn(true);
            mocked.when(ServerVersion::getServerType).thenCallRealMethod(); // Bypass mocking

            assertTrue(ServerVersion.getServerType().equals(ServerType.WATERFALL));

            mocked.verify(ServerVersion::isBukkit);
            mocked.verify(ServerVersion::isWaterfall);
            mocked.verify(ServerVersion::getServerType);
        }
    }

    @Test
    public void testGetServerTypeBungee() {
        try (MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)) {
            // Set stub return values when methods are called
            mocked.when(ServerVersion::isBukkit).thenReturn(false);
            mocked.when(ServerVersion::isWaterfall).thenReturn(false);
            mocked.when(ServerVersion::getServerType).thenCallRealMethod(); // Bypass mocking

            assertTrue(ServerVersion.getServerType().equals(ServerType.BUNGEE));

            mocked.verify(ServerVersion::isBukkit);
            mocked.verify(ServerVersion::isWaterfall);
            mocked.verify(ServerVersion::getServerType);
        }
    }
}
