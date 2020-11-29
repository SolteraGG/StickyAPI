/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.command.executor;

import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import com.dumbdogdiner.stickyapi.common.command.ExitCode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@FunctionalInterface
public interface Executor<CS> {
    ExitCode execute(@NotNull CS sender, @NotNull Arguments args, @NotNull String label, @NotNull Map<String, String> vars);
}