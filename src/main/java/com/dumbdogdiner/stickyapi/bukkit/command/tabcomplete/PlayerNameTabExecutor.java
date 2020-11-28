/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command.tabcomplete;

import com.dumbdogdiner.stickyapi.bukkit.util.PlayerUtil;
import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerNameTabExecutor implements TabExecutor {

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull Arguments arguments) {
        if (sender instanceof ConsoleCommandSender) {
            return PlayerUtil.Names.getOnlinePlayers();
        }

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        List<String> matchedPlayers = PlayerUtil.Names.getVisibleOnlinePlayers(senderPlayer, PlayerUtil.Comparators.BY_ALPHABETICAL);
        matchedPlayers.removeIf(s -> !StringUtil.startsWithIgnoreCase(s, arguments.getLastArgument()));

        return matchedPlayers;
    }

}
