/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.dumbdogdiner.stickyapi.common.ServerVersion.ServerType;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ServerVersionTest {

    @Test
    public void testGetServerTypeBukkit() {
        try (
            MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)
        ) {
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
        try (
            MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)
        ) {
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
        try (
            MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)
        ) {
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
        try (
            MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)
        ) {
            // Set stub return values when methods are called
            mocked.when(ServerVersion::isBukkit).thenReturn(false);
            mocked.when(ServerVersion::isWaterfall).thenReturn(true);
            mocked.when(ServerVersion::getServerType).thenCallRealMethod(); // Bypass mocking

            assertTrue(
                ServerVersion.getServerType().equals(ServerType.WATERFALL)
            );

            mocked.verify(ServerVersion::isBukkit);
            mocked.verify(ServerVersion::isWaterfall);
            mocked.verify(ServerVersion::getServerType);
        }
    }

    @Test
    public void testGetServerTypeBungee() {
        try (
            MockedStatic<ServerVersion> mocked = mockStatic(ServerVersion.class)
        ) {
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

    @Test
    public void testGetBukkitVersion() {
        String mockedVersion = "1.2.3-MOCKED";
        try (MockedStatic<Bukkit> mocked = mockStatic(Bukkit.class)) {
            mocked.when(Bukkit::getVersion).thenReturn(mockedVersion);

            assertEquals(ServerVersion.getBukkitVersion(), mockedVersion);

            mocked.verify(Bukkit::getVersion);
        }
    }

    @Test
    public void testGetBukkitVersionException() {
        try (MockedStatic<Bukkit> mocked = mockStatic(Bukkit.class)) {
            mocked
                .when(Bukkit::getVersion)
                .thenThrow(new NoClassDefFoundError());

            assertEquals(ServerVersion.getBukkitVersion(), null);

            mocked.verify(Bukkit::getVersion);
        }
    }

    @Test
    public void testGetBungeeVersion() {
        String mockedVersion =
            "git:BungeeCord-Bootstrap:1.2.3-MOCKED:12345:6789";

        ProxyServer proxyServer = mock(ProxyServer.class);

        try (MockedStatic<ProxyServer> mocked = mockStatic(ProxyServer.class)) {
            mocked.when(ProxyServer::getInstance).thenReturn(proxyServer);

            when(proxyServer.getVersion()).thenReturn(mockedVersion);

            assertEquals(ServerVersion.getBungeeVersion(), mockedVersion);

            verify(proxyServer).getVersion();
            mocked.verify(ProxyServer::getInstance);
        }
    }

    @Test
    public void testGetBungeeVersionException() {
        ProxyServer proxyServer = mock(ProxyServer.class);

        try (MockedStatic<ProxyServer> mocked = mockStatic(ProxyServer.class)) {
            mocked.when(ProxyServer::getInstance).thenReturn(proxyServer);

            when(proxyServer.getVersion())
                .thenThrow(new NoClassDefFoundError());

            assertEquals(ServerVersion.getBungeeVersion(), null);

            verify(proxyServer).getVersion();
            mocked.verify(ProxyServer::getInstance);
        }
    }
}
