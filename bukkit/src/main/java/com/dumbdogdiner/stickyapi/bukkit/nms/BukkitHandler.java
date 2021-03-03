/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.nms;

import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @deprecated This will be superseded and removed in a future release.
 */
@Deprecated
public class BukkitHandler {
    private BukkitHandler() {
    }

    /**
     * <p>
     * Extracts the NMS version from the Bukkit server package
     * </p>
     *
     * @return the NMS version
     */
    public static String getNmsVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    /**
     * <p>
     * Gets a class from the <code>org.bukkit.craftbukkit</code> package
     * </p>
     *
     * @param nmsClass CraftBukkit subclass
     * @return Class object of the specified CraftBukkit subclass.
     * @throws ClassNotFoundException if class does not exist
     */
    public static Class<?> getCraftClass(String nmsClass) throws ClassNotFoundException {
        String version = getNmsVersion();
        if(version == null)
            throw new ClassNotFoundException("Could not find Class " + nmsClass + ": Version is null.");

        String pkg = "org.bukkit.craftbukkit." + version + "." + nmsClass;
        Class<?> clazz = Class.forName(pkg);

        return clazz;
    }

    /**
     * <p>
     * Gets the CraftBukkit connection object of a player.
     * </p>
     *
     * @param player Bukkit player object.
     * @return Object of player connection.
     */
    public static Object getConnection(@NotNull Player player) {
        try {
            Object nmsPlayer = getCraftPlayer(player);
            Field conField = nmsPlayer.getClass().getField("playerConnection");
            Object con = conField.get(nmsPlayer);

            return con;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * <p>
     * Gets a class from the <code>net.minecraft.server</code> package
     * </p>
     *
     * @param nmsClass CraftBukkit subclass
     * @return Class object of the specified CraftBukkit subclass.
     * @throws ClassNotFoundException when class does not exist
     */
    public static Class<?> getNMSClass(String nmsClass) throws ClassNotFoundException {
        String version = getNmsVersion();
        if(version == null)
            throw new ClassNotFoundException("Could not find Class " + nmsClass + ": Version is null.");
        String pkg = "net.minecraft.server." + version + "." + nmsClass;
        return Class.forName(pkg);
    }

    /**
     * <p>
     * Gets the CraftBukkit instance of a player.
     * </p>
     *
     * @param player Bukkit player object.
     * @return Object of CraftBukkit player.
     */
    public static Object getCraftPlayer(@NotNull Player player) {
        try {
            return player.getClass().getMethod("getHandle").invoke(player);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * <p>
     * Send a packet to a player
     * </p>
     *
     * @param player Bukkit player object
     * @param packet Packet object (Must be of type <code>net.minecraft.server.[VERSION].Packet</code>)
     */
    public static void sendPacket(@NotNull Player player, @NotNull Object packet) {
        try {
            Class<?> packetClass = getNMSClass("Packet");

            Class<?> nmsPacket = getNMSClass("Packet");
            Object conn = getConnection(player);

            Method sendPacket = conn.getClass().getMethod("sendPacket", nmsPacket);
            sendPacket.invoke(conn, packet);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Injects code into a players incoming packet stream using {@link io.netty.channel.ChannelDuplexHandler}.
     * It's recommended to run this once inside a {@link org.bukkit.event.player.PlayerJoinEvent} listener.
     * </p>
     *
     * @param service Code that should be executed
     * @param player Bukkit player object
     */
    public static void injectPlayer(PacketInjector.InjectionService service, @NotNull Player player) {
        PacketInjector packetInjector = new PacketInjector();
        packetInjector.addService(service, player);
    }

    /**
     * <p>
     * Remove an active code injector from a players incoming packet stream.
     * If {@link BukkitHandler#injectPlayer(PacketInjector.InjectionService, Player)} was used, it is recommended
     * to call this method where needed, to avoid negative side affects.
     * </p>
     *
     * @param player Bukkit player object
     */
    public static void removeInjection(@NotNull Player player) {
        try {
            PacketInjector packetInjector = new PacketInjector();
            Channel ch = packetInjector.getChannel(packetInjector.getNetworkManager(player));

            if(ch.pipeline().get(player.getName()) != null) {
                ch.pipeline().remove(player.getName());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
