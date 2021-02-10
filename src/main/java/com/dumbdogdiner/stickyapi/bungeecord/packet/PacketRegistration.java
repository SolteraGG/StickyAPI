/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.packet;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.protocol.Protocol;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @deprecated This is unsafe.
 */
@Deprecated
public class PacketRegistration {
    private PacketRegistration() {
    }

    private static Method map, regPacket;
    private static Class<?> protocolMapping, protocolMappingArray;
    private static Object TO_CLIENT;
    private static @NotNull Boolean soundRegistered = false; // If this packet has been registered already

    // Process our reflection nonsense
    private static void processReflection() {
        try {
            @NotNull Field f = Protocol.class.getDeclaredField("TO_CLIENT");
            map = Protocol.class.getDeclaredMethod("map", int.class, int.class);
            f.setAccessible(true);
            map.setAccessible(true);
            protocolMapping = map.getReturnType();
            protocolMappingArray = Array.newInstance(protocolMapping, 0).getClass();
            TO_CLIENT = f.get(Protocol.GAME);
            regPacket = TO_CLIENT.getClass().getDeclaredMethod("registerPacket", Class.class, protocolMappingArray);
            regPacket.setAccessible(true);
        } catch (@NotNull IllegalAccessException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register a packet with BungeeCord
     * 
     * @param clazz that extends {@link net.md_5.bungee.protocol.DefinedPacket}
     * @param id    the protocol ID for the packet (see: https://wiki.vg/Protocol)
     */
    public static void registerPacket(Class<?> clazz, Integer id) {
        Object @NotNull [] array = (Object[]) Array.newInstance(protocolMapping, 1);
        try {
            array[0] = map.invoke(null, ProxyServer.getInstance().getProtocolVersion(), id);
            regPacket.invoke(TO_CLIENT, clazz, array);
        } catch (@NotNull IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // static {
    // registerSoundPacket();
    // }

    /**
     * Register our sound packet with BungeeCord
     */
    public static void registerSoundPacket() {
        if (soundRegistered) // We don't need to do this stuff twice...
            return;

        soundRegistered = true;
        processReflection();
        registerPacket(SoundPacket.class, com.dumbdogdiner.stickyapi.bungeecord.protocol.Protocol.getSoundEffectId()); // This
                                                                                                                       // should
                                                                                                                       // automatically
                                                                                                                       // register
                                                                                                                       // with
                                                                                                                       // the
                                                                                                                       // server's
                                                                                                                       // correct
                                                                                                                       // version
    }
}
