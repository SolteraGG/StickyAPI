/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.command.tabcomplete;

import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@FunctionalInterface
public interface TabExecutor<T> {
    @NotNull
    List<String> tabComplete(@NotNull T sender, @NotNull String commandLabel, @NotNull Arguments arguments);
}
