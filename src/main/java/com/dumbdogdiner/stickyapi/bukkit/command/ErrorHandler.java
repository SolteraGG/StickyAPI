/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import com.dumbdogdiner.stickyapi.common.command.ExitCode;
import org.bukkit.command.CommandSender;

import java.util.Map;

@FunctionalInterface
public interface ErrorHandler {
    /**
     * Provides handeling of a given ExitCode
     * @param exitCode the {@link ExitCode} from the execution
     * @param sender the {@link CommandSender} who ran the command
     * @param args the {@link Arguments} object from the command
     * @param vars the {@link com.dumbdogdiner.stickyapi.common.translation.LocaleProvider} variables used
     * @return if the error was handled by this {@link ErrorHandler}
     */
    boolean onError(ExitCode exitCode, CommandSender sender, Arguments args, Map<String, String> vars);
}