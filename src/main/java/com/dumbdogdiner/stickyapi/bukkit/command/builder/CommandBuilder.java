/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command.builder;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.bukkit.command.ErrorHandler;
import com.dumbdogdiner.stickyapi.bukkit.command.StickyPluginCommand;
import com.dumbdogdiner.stickyapi.bukkit.command.executor.Executor;
import com.dumbdogdiner.stickyapi.bukkit.command.tabcomplete.TabExecutor;
import com.dumbdogdiner.stickyapi.bukkit.plugin.StickyPlugin;
import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import com.dumbdogdiner.stickyapi.common.command.ExitCode;
import com.dumbdogdiner.stickyapi.common.command.builder.CommandBuilderBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * CommandBuilder for avoiding bukkit's terrible command API and making creating
 * new commands as simple as possible
 *
 * @since 2.0
 */
public class CommandBuilder extends CommandBuilderBase<CommandBuilder> {

    // Hmm...
    HashMap<CommandSender, Long> cooldownSenders = new HashMap<>();

    Executor executor;
    TabExecutor tabExecutor;

    ErrorHandler errorHandler;
    private StickyPluginCommand bukkitCommand;


    /**
     * Generate a permission for a given command
     *
     * @param owner The plugin that owns the command
     * @param name   The name of the command
     * @return the permission of format &lt;PluginName&gt;.&lt;CommandName&gt;
     */
    public static String getBasePermissionName(StickyPlugin owner, String name) {
        return (owner.getName() + "." + name).toLowerCase();
    }


    /**
     * Create a new {@link CommandBuilder} instance
     * <p>
     * Used to build and register Bukkit commands
     *
     * @param name The name of the command
     */
    public CommandBuilder(@NotNull String name) {
        super(name);
    }

    /**
     * Set the executor of the command
     *
     * @param executor to set
     * @return {@link CommandBuilder}
     */
    public CommandBuilder onExecute(@NotNull Executor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Set the tab complete executor of the command
     *
     * @param executor to set
     * @return {@link CommandBuilder}
     */
    public CommandBuilder onTabComplete(@NotNull TabExecutor executor) {
        this.tabExecutor = executor;
        return this;
    }

    /**
     * Set the error handler of the command
     *
     * @param handler to set
     * @return {@link CommandBuilder}
     */
    public CommandBuilder onError(@NotNull ErrorHandler handler) {
        this.errorHandler = handler;
        return this;
    }

    /**
     * Build the command!
     *
     * @param plugin to build it for
     * @return {@link org.bukkit.command.Command}
     */
    public StickyPluginCommand build(@NotNull StickyPlugin plugin) {

        if (getPermission() == null) {
            setPermission(getBasePermissionName(plugin, getName()));
        }
        StickyPluginCommand command = new StickyPluginCommand(getName(), getAliases(), plugin, new Permission(getPermission()), getPlaySound()) {
            @Override
            public ExitCode execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull Arguments args, @NotNull Map<String, String> variables) {
                return executor.execute(sender, args, alias, variables);
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException, CommandException {
                return tabExecutor.tabComplete(sender, alias, new Arguments(Arrays.asList(args)));
            }

            @Override
            public void onError(CommandSender sender, String commandLabel, Arguments arguments, ExitCode code, Map<String, String> vars) {
                if (errorHandler == null || !errorHandler.onError(code, sender, arguments, vars))
                    super.onError(sender, commandLabel, arguments, code, vars);
            }
        };

        command.setDescription(getDescription());


        // Execute the command by creating a new CommandExecutor and passing the
        // arguments to our executor


        command.setDescription(getDescription());

        command.setAliases(getAliases());

        for (var subCommand : getSubCommands().entrySet()) {
            command.getSubCommands().put(subCommand.getKey(), subCommand.getValue().build(plugin));
        }

        this.bukkitCommand = command;
        return command;
    }

    /**
     * Register the command with a {@link org.bukkit.plugin.Plugin}
     *
     * @param plugin to register with
     */
    public void register(@NotNull StickyPlugin plugin) {
        if(bukkitCommand == null || !plugin.equals(bukkitCommand.getPlugin())) {
            this.build(plugin);
        }
        bukkitCommand.register();
    }
}
