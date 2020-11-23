/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command.builder;

import org.jetbrains.annotations.NotNull;

public class SubComandBuilder extends CommandBuilder {
    /**
     * Create a new [@link SubCommandBuilder} instance
     * <p>
     * Used to build and register Bukkit commands
     *
     * @param name Name of subcommand
     */
    public SubComandBuilder(@NotNull String name) {
        super(name);
        //FIXME temp
        subCommand = true;
    }
}
