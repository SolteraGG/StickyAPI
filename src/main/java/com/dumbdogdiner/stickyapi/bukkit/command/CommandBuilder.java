/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.FutureTask;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.common.util.ReflectionUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * CommandBuilder for avoiding bukkit's terrible command API and making creating
 * new commands as simple as possible
 * 
 * @since 2.0
 */
public class CommandBuilder {

    Boolean synchronous = false;
    String name;
    String permission;
    String description;
    List<String> aliases = new ArrayList<>();

    Executor executor;
    TabExecutor tabExecutor;

    ErrorHandler errorHandler;

    Command bukkitCommand;

    HashMap<String, CommandBuilder> subCommands = new HashMap<>();

    @FunctionalInterface
    public interface Executor {
        public ExitCode apply(CommandSender sender, String commandLabel, List<String> args);
    }

    public interface TabExecutor {
        public java.util.List<String> apply(CommandSender sender, String commandLabel, List<String> args);
    }

    public interface ErrorHandler {
        public void apply(ExitCode exitCode, CommandSender sender, String commandLabel, List<String> args);
    }

    /**
     * Create a new [@link CommandBuilder} instance
     * <p>
     * Used to build and register Bukkit commands
     * 
     * @param name
     */
    public CommandBuilder(@NotNull String name) {
        this.name = name;
    }

    /**
     * If this command should run asynchronously
     * 
     * @param asynchronous
     * @return {@link CommandBuilder}
     */
    public CommandBuilder synchronous(@NotNull Boolean asynchronous) {
        this.synchronous = asynchronous;
        return this;
    }

    /**
     * Set this command to run asynchronously
     * 
     * @return {@link CommandBuilder}
     */
    public CommandBuilder synchronous() {
        return this.synchronous(true);
    }

    /**
     * Set the permission of the command
     * 
     * @param permission to set
     * @return {@link CommandBuilder}
     */
    public CommandBuilder permission(@NotNull String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Set the description of the command
     * 
     * @param description to set
     * @return {@link CommandBuilder}
     */
    public CommandBuilder description(@NotNull String description) {
        this.description = description;
        return this;
    }

    /**
     * Add an alias to this command.
     * 
     * @param alias to add
     * @return {@link CommandBuilder}
     */
    public CommandBuilder alias(@NotNull String... alias) {
        for (var a : alias) {
            this.aliases.add(a);
        }
        return this;
    }

    /**
     * Set the aliases of the command
     * 
     * @param aliases to set
     * @return {@link CommandBuilder}
     */
    public CommandBuilder aliases(@NotNull List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    /**
     * Add a subcommand to a command
     * @param builder the sub command
     * @return {@link CommandBuilder}
     */
    public CommandBuilder subCommand(CommandBuilder builder) {
        builder.synchronous = this.synchronous;
        this.subCommands.put(builder.name, builder);
        return this;
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

    private void performAsynchronousExecution(CommandSender sender, org.bukkit.command.Command command, String label, List<String> args) {
        StickyAPI.getPool().execute(new FutureTask<Void>(() -> {
            performExecution(sender, command, label, args);
            return null;
        }));
    }

    /**
     * Execute this command. Checks for existing sub-commands, and runs the error
     * handler if anything goes wrong.
     */
    private void performExecution(CommandSender sender, org.bukkit.command.Command command, String label, List<String> args) {
        // look for subcommands
        if (args.size() > 0 && subCommands.containsKey(args.get(0))) {
            CommandBuilder subCommand = subCommands.get(args.get(0));
            if (!synchronous && subCommand.synchronous) {
                throw new RuntimeException("Attempted to asynchronously execute a synchronous sub-command!");
            }

            // We can't modify List, so we need to make a clone of it, because java is special.
            ArrayList<String> argsClone = new ArrayList<String>(args);
            argsClone.remove(0);

            // spawn async command from sync
            if (synchronous && !subCommand.synchronous) {
                subCommand.performAsynchronousExecution(sender, command, label, argsClone);
            }

            subCommand.performExecution(sender, command, label, argsClone);
            return;
        }

        ExitCode exitCode;
        try {
            // If the user does not have permission to execute the sub command, don't let them execute and return permission denied
            if (this.permission != null && !sender.hasPermission(this.permission)) {
                exitCode = ExitCode.EXIT_PERMISSION_DENIED;
            }
            else {
                exitCode = executor.apply(sender, label, args);
            }
        } catch (Exception e) {
            exitCode = ExitCode.EXIT_ERROR;
            e.printStackTrace();
        }

        // run the error handler - something went wrong!
        if (exitCode != ExitCode.EXIT_SUCCESS)
            errorHandler.apply(exitCode, sender, label, args);
    }

    /**
     * Build the command!
     * 
     * @param plugin to build it for
     * @return {@link org.bukkit.command.Command}
     */
    public org.bukkit.command.Command build(@NotNull Plugin plugin) {
        PluginCommand command;

        if (this.synchronous == null) {
            this.synchronous = false;
        }

        try {
            // PluginCommand is protected, so we need to use reflection to instantiate an
            // instance of it...
            // This is kind of annoying, since it involves... well... more reflection... But
            // oh well ¯\_(ツ)_/¯
            Constructor<?> c = ReflectionUtil.getProtectedConstructor(PluginCommand.class, String.class, Plugin.class);
            command = (PluginCommand) c.newInstance(this.name, plugin);

            // Some funky shit is going on.
            if (command == null)
                return null;

            // Execute the command by creating a new CommandExecutor and passing the
            // arguments to our executor
            command.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label,
                        String[] args) {
                    // this isn't in this scope
                    // java is weird about it
                    // k
                    performExecution(sender, command, label, Arrays.asList(args));
                    return true;
                }
            });

            command.setTabCompleter(new TabCompleter() {
                @Override
                public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                    return tabExecutor.apply(sender, alias, Arrays.asList(args));
                }
            });

            command.setDescription(this.description);

            if (this.aliases != null)
                command.setAliases(this.aliases);

            command.setPermission(this.permission);
            this.bukkitCommand = command;
            return command;

        } catch (Exception e) {
            // If some more funky shit happens, let's just print our stacktrace and return
            // null, there's not much else we can do
            // Although, in theory, we should never run into this issue.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Register the command with a {@link org.bukkit.plugin.Plugin}
     * 
     * @param plugin to register with
     */
    public void register(@NotNull Plugin plugin) {
        Command command = this.build(plugin);
        CommandMap cmap = ReflectionUtil.getProtectedValue(plugin.getServer(), "commandMap");
        cmap.register(plugin.getName(), command);
    }
}
