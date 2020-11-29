/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command.tabcomplete;


import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class NonCompletingTabExecutor implements TabExecutor {
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull Arguments arguments) {
        return Collections.emptyList();
    }
}
