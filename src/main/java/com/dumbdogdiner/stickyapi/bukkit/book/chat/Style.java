/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book.chat;

import lombok.Data;
import org.bukkit.ChatColor;

@Data
public class Style {
    boolean obfuscated;
    boolean bold;
    boolean italic;
    boolean underline;
    boolean strikethrough;
}
