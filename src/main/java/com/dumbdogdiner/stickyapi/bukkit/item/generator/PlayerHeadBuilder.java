/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dumbdogdiner.stickyapi.bukkit.user.StickyUserBukkit;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.common.webapis.CachedMojangAPI;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotCall;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * This class provides an easy way to construct Player Heads from existing players, whether online or offline
 */
public class PlayerHeadBuilder extends SkullBuilder {
    private @NotNull SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
    private final @NotNull StickyUserBukkit player;
    private final @NotNull PlayerProfile ownerProfile;
    private boolean frozen;


    /**
     * @param playerId the UUID of the player whose head should be generated
     */
    public PlayerHeadBuilder(@NotNull UUID playerId) {
        this.meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerId));
        this.ownerProfile = Bukkit.getServer().createProfile(playerId);
        this.player = new StickyUserBukkit(playerId);
    }

    /**
     * @param player The player to use for head generation
     */
    public PlayerHeadBuilder(@NotNull Player player) {
        this.meta.setOwningPlayer(player);
        this.ownerProfile = player.getPlayerProfile();
        this.player = new StickyUserBukkit(player);
    }

    /**
     * @param player The player to use for head generation
     */
    public PlayerHeadBuilder(@NotNull StickyUserBukkit player) {
        this.meta.setOwningPlayer(player.getAsBukkitPlayer());
        this.ownerProfile = player.getAsBukkitPlayer().getPlayerProfile();
        this.player = new StickyUserBukkit(player);
    }

    /**
     * @param player The Offline Player to use for head generation
     */
    public PlayerHeadBuilder(@NotNull OfflinePlayer player) {
        this.meta.setOwningPlayer(player);
        this.ownerProfile = Bukkit.createProfile(player.getUniqueId());
        this.player = new StickyUserBukkit(player);
    }

    /**
     * Creates a {@link PlayerHeadBuilder} from an existing head
     *
     * @param head an existing playerhead
     * @throws IllegalArgumentException if the {@link ItemStack} is not a head, has bad metadata, or the profile attached is invalid
     */
    public PlayerHeadBuilder(@NotNull ItemStack head) {
        Preconditions.checkArgument(head.getType() != Material.PLAYER_HEAD
                        && head.getType() != Material.PLAYER_WALL_HEAD,
                "Head must be a player head or player wall head");
        meta = (SkullMeta) head.getItemMeta();
        Preconditions.checkNotNull(meta, "Player head must have metadata attached");
        ownerProfile = meta.getPlayerProfile();
        name(meta.getDisplayName());
        Preconditions.checkNotNull(ownerProfile, "The player head must have a PlayerProfile attached");
        if (!Objects.requireNonNull(ownerProfile).hasTextures()) {
            if (!ownerProfile.complete()) {
                throw new IllegalArgumentException("Invalid player profile attached to the head, with no UUID or textures!");
            }
        }
        player = new StickyUserBukkit(ownerProfile.getId());
    }

    /**
     * Statically sets the texture of the head so it will not update in the future
     */
    public @NotNull PlayerHeadBuilder freeze() {
        if (player != null) {
            super.texture(CachedMojangAPI.getSkinTexture(player));
            frozen = true;
        }
        return this;
    }

    /**
     * Constructs a new {@link ItemStack} of type {@link Material#PLAYER_HEAD} based off the specified player
     *
     * @return A new player head {@link ItemStack}
     */
    @Override
    public @NotNull ItemStack build() {
        if (name != null) {
            meta.setDisplayName(name);
        } else {
            meta.setDisplayName(StringUtil.capitalize(player.getAsBukkitPlayer().getDisplayName()));
        }

        if (frozen) {
            ownerProfile.setProperty(new ProfileProperty("texture", texture));
        }
        @NotNull ItemStack head = new ItemStack(Material.PLAYER_HEAD, quantity);
        head.setItemMeta(meta);

        return head;
    }


    // Disable methods of superclass:

    /**
     * This is unsupported.
     *
     * @throws UnsupportedOperationException if ran
     */
    @DoNotCall
    @Override
    @Deprecated
    public @NotNull SkullBuilder texture(@NotNull String str) {
        throw new UnsupportedOperationException();
    }

    /**
     * This is unsupported.
     *
     * @throws UnsupportedOperationException if ran
     */
    @DoNotCall
    @Override
    @Deprecated
    public @NotNull SkullBuilder head(@NotNull String str) {
        throw new UnsupportedOperationException();
    }

    /**
     * This is unsupported.
     *
     * @throws UnsupportedOperationException if ran
     */
    @DoNotCall
    @Override
    @Deprecated
    public @NotNull SkullBuilder texture(java.net.@NotNull URL url) {
        throw new UnsupportedOperationException();
    }

    /**
     * This is unsupported.
     *
     * @throws UnsupportedOperationException if ran
     */
    @DoNotCall
    @Deprecated
    @Override
    public @NotNull SkullBuilder category(@NotNull String str) {
        throw new UnsupportedOperationException();
    }
}
