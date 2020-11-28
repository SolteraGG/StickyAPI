/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.packet;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.protocol.Protocol;

public class PacketRegistration {

    private static Method map, regPacket;
    private static Class<?> protocolMapping, protocolMappingArray;
    private static Object TO_CLIENT;

    // Process our reflection nonsense
    private static void processReflection() {
        try {
            Field f = Protocol.class.getDeclaredField("TO_CLIENT");
            map = Protocol.class.getDeclaredMethod("map", int.class, int.class);
            f.setAccessible(true);
            map.setAccessible(true);
            protocolMapping = map.getReturnType();
            protocolMappingArray = Array.newInstance(protocolMapping, 0).getClass();
            TO_CLIENT = f.get(Protocol.GAME);
            regPacket = TO_CLIENT.getClass().getDeclaredMethod("registerPacket", Class.class, protocolMappingArray);
            regPacket.setAccessible(true);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException e) {
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
        Object[] array = (Object[]) Array.newInstance(protocolMapping, 1);
        try {
            array[0] = map.invoke(null, ProxyServer.getInstance().getProtocolVersion(), id);
            regPacket.invoke(TO_CLIENT, clazz, array);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static {
        processReflection();
        registerSoundPacket();
    }

    /**
     * Register our sound packet with BungeeCord
     */
    public static void registerSoundPacket() {
        registerPacket(SoundPacket.class, com.dumbdogdiner.stickyapi.bungeecord.protocol.Protocol.getSoundEffectId()); // This should automatically register with the server's correct version
    }
}
