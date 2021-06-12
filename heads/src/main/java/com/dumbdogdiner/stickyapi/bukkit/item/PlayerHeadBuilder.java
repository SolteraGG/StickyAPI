/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.annotation.DoNotCall;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.webapi.mojang.MojangAPI;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * This class provides an easy way to construct Player Heads from existing players, whether online or offline
 */
public class PlayerHeadBuilder {
    private @NotNull SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
    private final UUID playerId;
    private final @NotNull PlayerProfile ownerProfile;
    private boolean frozen;
    // Internal builder
    private SkullBuilder builder;


    /**
     * @param playerId the UUID of the player whose head should be generated
     */
    public PlayerHeadBuilder(@NotNull UUID playerId) {
        this.meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerId));
        this.ownerProfile = Bukkit.getServer().createProfile(playerId);
        Preconditions.checkNotNull(playerId);
        this.playerId = playerId;
    }

    /**
     * @param player The player to use for head generation
     */
    public PlayerHeadBuilder(@NotNull Player player) {
        this.meta.setOwningPlayer(player);
        this.ownerProfile = player.getPlayerProfile();
        playerId = player.getUniqueId();
    }

    /**
     * @param player The Offline Player to use for head generation
     */
    public PlayerHeadBuilder(@NotNull OfflinePlayer player) {
        this.meta.setOwningPlayer(player);
        this.ownerProfile = Bukkit.createProfile(player.getUniqueId());
        this.playerId = player.getUniqueId();
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
        // We check this almost immediately after, so it's OK if it's null temporarily
        //noinspection ConstantConditions
        ownerProfile = meta.getPlayerProfile();
        builder.name(meta.getDisplayName());
        Preconditions.checkNotNull(ownerProfile, "The player head must have a PlayerProfile attached");
        if (!Objects.requireNonNull(ownerProfile).hasTextures()) {
            if (!ownerProfile.complete()) {
                throw new IllegalArgumentException("Invalid player profile attached to the head, with no UUID or textures!");
            }
        }
        this.playerId = null;
    }

    /**
     * Statically sets the texture of the head so it will not update in the future
     */
    public @NotNull PlayerHeadBuilder freeze() {
        if (playerId != null) {
            try {
                String textureString = MojangAPI.getTextureString(playerId);
                assert textureString != null;
                builder.texture(textureString);
                frozen = true;
            } catch (HttpException e){
                Bukkit.getLogger().log(Level.WARNING, e.getMessage());
            }
        }
        return this;
    }

    /**
     * Constructs a new {@link ItemStack} of type {@link Material#PLAYER_HEAD} based off the specified player
     *
     * @return A new player head {@link ItemStack}
     */
    public @NotNull ItemStack build() {
        meta.setDisplayName(Objects.requireNonNullElseGet(builder.name(), () -> {
            try {
                return ChatColor.YELLOW + MojangAPI.getUsername(playerId) + "'s Head";
            } catch (HttpException e) {
                Bukkit.getLogger().log(Level.WARNING, e.getMessage());
                return ChatColor.YELLOW + Material.PLAYER_HEAD.name();
            }
        }));

        if (frozen) {
            ownerProfile.setProperty(new ProfileProperty("texture", builder.texture()));
        }
        @NotNull ItemStack head = new ItemStack(Material.PLAYER_HEAD, builder.quantity());
        head.setItemMeta(meta);

        return head;
    }

    public PlayerHeadBuilder quantity(int qty) {
        builder.quantity(qty);
        return this;
    }
}
