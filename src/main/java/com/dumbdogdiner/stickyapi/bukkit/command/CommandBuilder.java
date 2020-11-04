package com.dumbdogdiner.stickyapi.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandBuilder {
    
    Boolean asynchronous = false;
    String permission;
    String name;

    interface Executor {
        public ExitCode execute(CommandSender sender, String commandLabel, String[] args);
    }
    
    interface ErrorHandler {
        public void handle(ExitCode exitCode);
    }

    public CommandBuilder(@NotNull String name) {
        this.name = name;
    }
    
    public CommandBuilder setAsynchronous(@NotNull Boolean asynchronous) {
        this.asynchronous = asynchronous;
        return this;
    }
    
    public CommandBuilder setPermission(@NotNull String permission) {
        return this;
    }

    public CommandBuilder onExecute(@NotNull Executor executor) {
        return this;
    }

    public CommandBuilder onError(@NotNull ErrorHandler handle) {
        return this;
    }

    public CommandBuilder onSyntaxError(@NotNull ErrorHandler handler) {
        return this;
    }
    
    public org.bukkit.command.Command build(Plugin plugin) {
        return null;
    }

    public org.bukkit.command.Command register(Plugin plugin) {
        // var command = this.build(plugin);
        // Bukkit.getCommandMap().register(fallbackPrefix, command);
        return null;
    }
}
