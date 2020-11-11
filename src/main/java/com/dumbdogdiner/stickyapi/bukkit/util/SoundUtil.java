/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.util;

import com.dumbdogdiner.stickyapi.StickyAPI;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the playing of cute fox noises for notification purposes.
 */
public class SoundUtil {
    /**
     * Check if the parsed sender can receive sounds. Returns true if valid.
     * @param sender {@link org.bukkit.command.CommandSender} The sender to validate
     * @return {@link java.lang.Boolean}
     */
    private static Boolean validate(CommandSender sender) {
        return sender instanceof Player;
    }

    /**
     * Queue a specific sound to be run at a later date.
     * @param player The player to play the sound to
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param delay T
     */
    public void queueSound(Player player, Sound sound, float volume, float pitch, Long delay) {
        StickyAPI.getPool().submit(() -> {
            try {
                Thread.sleep(delay);
                player.playSound(player.getLocation(), sound, volume, pitch);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Send an info notification to the target player.
     * @param player {@link org.bukkit.entity.Player} The target player
     */
    public void sendInfo(Player player) {
        queueSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f, 0L);
        queueSound(player, Sound.ENTITY_FOX_AMBIENT, 1f, 1f, 500L);
    }

    /**
     * Send a quiet notification to the target player.
     * @param player {@link org.bukkit.entity.Player} The target player
     */
    public void sendQuiet(Player player) {
        queueSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f,1f,0L);
        // This makes it too loud?
        // queueSound(player, Sound.ENTITY_FOX_SLEEP, 1f, 1f, 500L);
    }

    /**
     * Send an error notification to the target player.
     * @param player {@link org.bukkit.entity.Player} The target player
     */
    public void sendError(Player player) {
        queueSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f,  0.944f, 0L);
        queueSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f, 0L);
        queueSound(player, Sound.ENTITY_FOX_HURT, 1f, 1f, 0L);
    }

    /**
     * Send a success notification to the target player.
     * @param player {@link org.bukkit.entity.Player} The target player
     */
    public void sendSuccess(Player player) {
        queueSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f, 0L);
        queueSound(player, Sound.ENTITY_FOX_SCREECH, 1f, 1f, 500L);
    }

    /**
     * Send a CommandSender the specified notification, if you can. Returns true if the sound was played.
     * @param sender {@link org.bukkit.command.CommandSender} The sender
     * @param type {@link com.dumbdogdiner.stickyapi.bukkit.util.NotificationType} The type of sound
     * @return {@link java.lang.Boolean}
     */
    public Boolean send(CommandSender sender, NotificationType type) {
        if (!validate(sender)) {
            return false;
        }
        var player = (Player) sender;
        switch (type) {
            case ERROR:sendError(player);
            case INFO: sendInfo(player);
            case QUIET: sendQuiet(player);
            case SUCCESS: sendSuccess(player);
        }
        return true;
    }
}
