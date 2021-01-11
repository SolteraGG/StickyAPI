/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.user;

import com.dumbdogdiner.stickyapi.bukkit.item.generator.PlayerHeadBuilder;
import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public class StickyUserBukkit extends StickyUser {
    public StickyUserBukkit(@NotNull Player p) {
        super(p.getUniqueId(), p.getName());
    }

    public StickyUserBukkit(@NotNull OfflinePlayer p) {
        super(p.getUniqueId(), Objects.requireNonNull(p.getName()));
    }

    public StickyUserBukkit(@NotNull StickyUser p){
        super(p);
    }

    public StickyUserBukkit(@NotNull UUID uniqueId){
        super(uniqueId);
    }

    public @Nullable Player getAsBukkitPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public @NotNull OfflinePlayer getAsOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    public boolean isOnline(){
        return getAsOfflinePlayer().isOnline();
    }

    public boolean isBanned(){
        return getAsOfflinePlayer().isBanned();
    }

    public boolean playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().playSound(location, sound, volume, pitch);
        return true;
    }

    public boolean sendMessage(String [] msgs){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendMessage(msgs);
        return true;
    }

    public boolean sendMessage(@NotNull String msg){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendMessage(msg);
        return true;
    }

    public boolean sendRawMessage(@NotNull String msg){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendRawMessage(msg);
        return true;
    }

    public @NotNull ItemStack getHead(){
        return getHead(1);
    }

    public @NotNull ItemStack getHead(int amt){
        @NotNull PlayerHeadBuilder gen = new PlayerHeadBuilder(this);
        gen.quantity(amt);
        return gen.build();
    }

    public @Nullable Player toBukkitPlayer(){
        return Bukkit.getPlayer(uniqueId);
    }

    public @NotNull OfflinePlayer toOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }
}
