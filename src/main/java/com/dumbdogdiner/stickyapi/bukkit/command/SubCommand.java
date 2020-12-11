/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a <i>static</i> field of a command class as a subcommand of that command. The name and aliases of the
 * subcommand become the flags that trigger the subcommand.
 * <br/>
 * Example:
 * <pre>{@code
 * class FooCommand extends StickyPluginCommand {
 *     ...
 *
 *     @SubCommand
 *     public static StickyPluginCommand subCommandA = new StickyPluginCommand("a", YourPlugin.getInstance()) {
 *         ...
 *     };
 * }
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {}
