/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.nms;

import io.netty.channel.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * Handles Packet Injection
 * 
 * @deprecated This will be superseded and removed in a future release.
 */
@Deprecated
public class PacketInjector {

    /**
     * @deprecated This will be superseded and removed in a future release.
     */
    @Deprecated
    public interface InjectionService {
        /**
         * <p>
         * Allows running code before an incoming packet is processed by the server.
         * </p>
         *
         * @param context {@link ChannelHandlerContext}
         * @param packet Represents the Packet object
         */
        void run(ChannelHandlerContext context, Object packet);
    }

    private Field networkManagerField;
    private Field channel;

    protected PacketInjector() {
        try {
            Class<?> playerConnection = BukkitHandler.getNMSClass("PlayerConnection");
            networkManagerField = playerConnection.getField("networkManager");

            Class<?> networkManager = BukkitHandler.getNMSClass("NetworkManager");
            channel = networkManager.getField("channel");

        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected void addService(InjectionService service, @NotNull Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                service.run(channelHandlerContext, packet);
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                super.write(ctx, msg, promise);
            }
        };

        try {
            Channel channel = getChannel(getNetworkManager(player));
            if(channel.pipeline().get(player.getName()) == null) {
                channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
            } else {
                throw new IllegalStateException("Channel '" + player.getName() + "' already exists.");
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * <p>
     * Gets the NetworkManager object of a player connection
     * </p>
     *
     * @param player Bukkit player object
     * @return the NetworkManager object
     * @throws IllegalAccessException may be thrown if the <code>networkManager</code> field can not be accessed
     */
    protected Object getNetworkManager(@NotNull Player player) throws IllegalAccessException {
        return networkManagerField.get(BukkitHandler.getConnection(player));
    }

    /**
     * <p>
     * Gets an active channel from a NetworkManager object
     * </p>
     *
     * @param networkManager the NetworkManager object
     * @return The {@link io.netty.channel.Channel} object.
     *         May return <code>null</code> if there is no {@link io.netty.channel.Channel} object
     *         or when NetworkManager is null.
     */
    protected Channel getChannel(@NotNull Object networkManager) {
        Channel ch = null;
        try {
            ch = (Channel) channel.get(networkManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ch;
    }

}
