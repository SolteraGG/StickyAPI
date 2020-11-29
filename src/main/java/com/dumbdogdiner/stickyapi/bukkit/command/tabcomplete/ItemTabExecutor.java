/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command.tabcomplete;

import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.common.base.Predicate;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemTabExecutor implements TabExecutor {
    private static final List<String> materialNames;
    static {
        materialNames = Stream.of(Material.values()).map(Material::name).collect(Collectors.toList());
        materialNames.sort(String.CASE_INSENSITIVE_ORDER);
    }
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull Arguments arguments) {
        ArrayList<String> tabList = new ArrayList<>(materialNames);
        tabList.removeIf((Predicate<String>) input -> {
            assert input != null;
            return StringUtil.startsWithIgnoreCase(input, arguments.getLastArgument());
        });

        return tabList;
    }
}
