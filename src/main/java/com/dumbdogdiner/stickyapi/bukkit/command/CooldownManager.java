/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CooldownManager {
    private final HashMap<CommandSender, Long> cooldowns = new HashMap<>();
    private ScheduledExecutorService scheduledClear;
    private final long COOLDOWN_TIMEOUT;

    /**
     * Get the remaining time on the cooldown (in milliseconds) for a sender
     *
     * @param sender The {@link org.bukkit.command.CommandSender} to check/add to cooldown
     * @return the number of milliseconds remaining in cooldown (0 if their cooldown had expired)
     */
    public long getSenderRemainingCooldown(CommandSender sender) {
        return Math.max(cooldowns.getOrDefault(sender, 0L) - System.currentTimeMillis(), 0L);
    }

    public void addSender(CommandSender sender) {
        cooldowns.put(sender, COOLDOWN_TIMEOUT + System.currentTimeMillis());
    }

    public CooldownManager(long cooldown_timeout) {
        COOLDOWN_TIMEOUT = cooldown_timeout;
    }

    // TODO make sure this doesn't make jvm hang!!
    private void enableAutoCleanup() {
        if (scheduledClear == null || scheduledClear.isShutdown()) {
            scheduledClear = Executors.newSingleThreadScheduledExecutor();
            scheduledClear.scheduleAtFixedRate(() -> {
                cooldowns.forEach((sender, exptime) -> {
                    if (exptime >= System.currentTimeMillis())
                        cooldowns.remove(sender);
                });
            }, 60, 60, TimeUnit.SECONDS);
        }
    }
}
