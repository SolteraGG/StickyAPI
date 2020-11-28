/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.commonmarkextensions;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.commonmark.node.CustomBlock;

public class MCColorFormatBlock extends CustomBlock {
    @Getter
    private ChatColor type;

    public MCColorFormatBlock(ChatColor type) {
        this.type = type;
    }
}
