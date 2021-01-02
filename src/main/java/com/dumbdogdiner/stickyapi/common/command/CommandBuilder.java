/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public abstract class CommandBuilder<T extends CommandBuilder<T>> {

    @Getter
    Boolean synchronous = false;
    @Getter
    Boolean requiresPlayer = false;
    @Getter
    String name;
    @Getter
    String permission;
    @Getter
    String description;
    @Getter
    Boolean playSound = false;
    @Getter
    List<String> aliases = new ArrayList<>();
    @Getter
    Long cooldown = 0L;
    @Getter
    HashMap<String, T> subCommands = new HashMap<>();

    /**
     * Create a new [@link CommandBuilder} instance
     * <p>
     * Used to build and register Bukkit commands
     * 
     * @param name The name of the command
     */
    public CommandBuilder(@NotNull String name) {
        this.name = name;
    }

    /**
     * If this command should run asynchronously
     * 
     * @param synchronous if this command should run synchronously
     * @return {@link CommandBuilder}
     */
    public T synchronous(@NotNull Boolean synchronous) {
        this.synchronous = synchronous;
        return (T) this;
    }

    /**
     * Set this command to run asynchronously
     * 
     * @return {@link CommandBuilder}
     */
    public T synchronous() {
        return this.synchronous(true);
    }

    /**
     * Set the cooldown for this command
     * 
     * @param cooldown in milliseconds
     * @return {@link CommandBuilder}
     */
    public T cooldown(@NotNull Long cooldown) {
        this.cooldown = cooldown;
        return (T) this;
    }

    /**
     * If this command requires the sender to be an instance of
     * {@link org.bukkit.entity.Player}
     * 
     * @param requiresPlayer If this command should require a player as the executor
     * @return {@link CommandBuilder}
     */
    public T requiresPlayer(@NotNull Boolean requiresPlayer) {
        this.requiresPlayer = requiresPlayer;
        return (T) this;
    }

    /**
     * If this command requires the sender to be an instance of
     * {@link org.bukkit.entity.Player}
     * 
     * @return {@link CommandBuilder}
     */
    public T requiresPlayer() {
        return this.requiresPlayer(true);
    }

    /**
     * If this command should play a sound upon exiting
     * 
     * @param playSound If this command should play a sound upon exiting
     * @return {@link CommandBuilder}
     */
    public T playSound(@NotNull Boolean playSound) {
        this.playSound = playSound;
        return (T) this;
    }

    /**
     * If this command should play a sound upon exiting
     * 
     * @return {@link CommandBuilder}
     */
    public T playSound() {
        return this.playSound(true);
    }

    /**
     * Set the permission of the command
     * 
     * @param permission to set
     * @return {@link CommandBuilder}
     */
    public T permission(@NotNull String permission) {
        this.permission = permission;
        return (T) this;
    }

    /**
     * Set the description of the command
     * 
     * @param description to set
     * @return {@link CommandBuilder}
     */
    public T description(@NotNull String description) {
        this.description = description;
        return (T) this;
    }

    /**
     * Add an alias to this command.
     * 
     * @param alias to add
     * @return {@link CommandBuilder}
     */
    public T alias(@NotNull String... alias) {
        for (var a : alias) {
            this.aliases.add(a);
        }
        return (T) this;
    }

    /**
     * Set the aliases of the command
     * 
     * @param aliases to set
     * @return {@link CommandBuilder}
     */
    public T aliases(@NotNull List<String> aliases) {
        this.aliases = aliases;
        return (T) this;
    }

    /**
     * Add a subcommand to a command
     * 
     * @param builder the sub command
     * @return {@link CommandBuilder}
     */
    public T subCommand(@NotNull T builder) {
        builder.synchronous = this.synchronous;
        this.subCommands.put(builder.name, builder);
        return (T) this;
    }
}
