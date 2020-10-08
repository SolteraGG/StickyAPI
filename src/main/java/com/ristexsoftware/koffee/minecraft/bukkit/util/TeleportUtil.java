package com.ristexsoftware.koffee.minecraft.bukkit.util;

import com.ristexsoftware.koffee.common.util.NumberUtil;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Various methods for teleporting entites in a minecraft world
 */
public class TeleportUtil {
    
    /**
     * Teleport the player to a random location in the world
     * @param player The player to teleport
     * @param maxX The max X coord range
     * @param maxZ The max Z coord range
     * <p>WARNING: This is unsafe! It may teleport player to dangerous locations (Like lava)
     */
    public static void teleportRandom(Player player, Integer maxX, Integer minX, Integer maxZ, Integer minZ) {
        int x = NumberUtil.getRandomNumber(minX, maxX);
        int z = NumberUtil.getRandomNumber(minZ, maxZ);
        player.teleport(new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z), z));
    }
}
