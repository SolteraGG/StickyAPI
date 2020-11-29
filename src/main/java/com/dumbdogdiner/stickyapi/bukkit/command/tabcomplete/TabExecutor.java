/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command.tabcomplete;

import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface TabExecutor extends com.dumbdogdiner.stickyapi.common.command.tabcomplete.TabExecutor<CommandSender> {
    @Override
    @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull Arguments arguments);
}
