/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
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

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public class StickyUserBukkit extends StickyUser {
    public StickyUserBukkit(Player p) {
        super(p.getUniqueId(), p.getName());
    }

    public StickyUserBukkit(OfflinePlayer p) {
        super(p.getUniqueId(), Objects.requireNonNull(p.getName()));
    }

    public StickyUserBukkit(StickyUser p){
        super(p);
    }

    public StickyUserBukkit(UUID uniqueId){
        super(uniqueId);
    }

    public Player getAsBukkitPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public OfflinePlayer getAsOfflinePlayer() {
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

    public boolean sendMessage(String msg){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendMessage(msg);
        return true;
    }

    public boolean sendRawMessage(String msg){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendRawMessage(msg);
        return true;
    }

    public ItemStack getHead(){
        return getHead(1);
    }
    public ItemStack getHead(int amt){
        PlayerHeadBuilder gen = new PlayerHeadBuilder(this);
        gen.quantity(amt);
        return gen.build();
    }


}
