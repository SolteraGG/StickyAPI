/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.FutureTask;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.bungeecord.packet.PacketRegistration;
import com.dumbdogdiner.stickyapi.bungeecord.util.SoundUtil;
import com.dumbdogdiner.stickyapi.arguments.Arguments;
import com.dumbdogdiner.stickyapi.command.ExitCode;
import com.dumbdogdiner.stickyapi.command.CommandBuilder;
import com.dumbdogdiner.stickyapi.util.NotificationType;
import com.dumbdogdiner.stickyapi.util.StringUtil;
import com.google.common.collect.ImmutableList;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("deprecation") // PacketRegistration is deprecated
public class BungeeCommandBuilder extends CommandBuilder<BungeeCommandBuilder> {

    // Hmm...
    HashMap<CommandSender, Long> cooldownSenders = new HashMap<>();

    Executor executor;
    TabExecutor tabExecutor;

    ErrorHandler errorHandler;
    Boolean playSound = false;

    @FunctionalInterface
    public interface Executor {
        public ExitCode apply(CommandSender sender, Arguments args, TreeMap<String, String> vars);
    }

    public interface TabExecutor {
        public java.util.List<String> apply(CommandSender sender, String commandLabel, Arguments args);
    }

    public interface ErrorHandler {
        public void apply(ExitCode exitCode, CommandSender sender, Arguments args, TreeMap<String, String> vars);
    }

    /**
     * If this command should play a sound upon exiting
     * 
     * @param playSound If this command should play a sound upon exiting
     * @return {@link CommandBuilder}
     * @deprecated I advise against using this since it plays the sound at an absurd
     *             volume since we can't get the location of the sender which means
     *             the sound is slightly distorted and unpleasant.
     */
    @Override
    @Deprecated
    public BungeeCommandBuilder playSound(@NotNull Boolean playSound) {
        this.playSound = playSound;
        PacketRegistration.registerSoundPacket(); // Register our sound packet, since this probably hasn't happened
                                                  // already and if we don't, sounds will not play
        return this;
    }

    /**
     * If this command should play a sound upon exiting
     * 
     * @return {@link CommandBuilder}
     * @deprecated I advise against using this since it plays the sound at an absurd
     *             volume since we can't get the location of the sender which means
     *             the sound is slightly distorted and unpleasant.
     */
    @Override
    @Deprecated
    public BungeeCommandBuilder playSound() {
        return this.playSound(true);
    }

    public BungeeCommandBuilder(@NotNull String name) {
        super(name);
    }

    private void performAsynchronousExecution(CommandSender sender, BungeeCommandBuilder builder, String label,
            List<String> args) {
        StickyAPI.getPool().execute(new FutureTask<Void>(() -> {
            performExecution(sender, builder, label, args);
            return null;
        }));
    }

    /**
     * Execute this command. Checks for existing sub-commands, and runs the error
     * handler if anything goes wrong.
     */
    private void performExecution(CommandSender sender, BungeeCommandBuilder builder, String label, List<String> args) {
        // look for subcommands
        if (args.size() > 0 && getSubCommands().containsKey(args.get(0))) {
            BungeeCommandBuilder subCommand = (BungeeCommandBuilder) getSubCommands().get(args.get(0));
            if (!getSynchronous() && subCommand.getSynchronous()) {
                throw new RuntimeException("Attempted to asynchronously execute a synchronous sub-command!");
            }

            // We can't modify List, so we need to make a clone of it, because java is
            // special.
            ArrayList<String> argsClone = new ArrayList<String>(args);
            argsClone.remove(0);

            // spawn async command from sync
            if (getSynchronous() && !subCommand.getSynchronous()) {
                subCommand.performAsynchronousExecution(sender, builder, label, argsClone);
            } else {
                subCommand.performExecution(sender, builder, label, argsClone);
            }

            return;
        }

        ExitCode exitCode;
        Arguments a = new Arguments(args);
        var variables = new TreeMap<String, String>();
        variables.put("command", builder.getName());
        variables.put("sender", sender.getName());
        variables.put("player", sender.getName());
        variables.put("uuid", (sender instanceof ProxiedPlayer) ? ((ProxiedPlayer) sender).getUniqueId().toString()
                : "00000000-0000-0000-0000-000000000000");
        variables.put("cooldown", getCooldown().toString());
        variables.put("cooldown_remaining",
                cooldownSenders.containsKey(sender)
                        ? String.valueOf(getCooldown() - (System.currentTimeMillis() - cooldownSenders.get(sender)))
                        : "0");
        try {
            if (cooldownSenders.containsKey(sender)
                    && ((System.currentTimeMillis() - cooldownSenders.get(sender)) < getCooldown())) {
                exitCode = ExitCode.EXIT_COOLDOWN;
            } else {

                // Add our sender and their command execution time to our hashmap of coolness
                cooldownSenders.put(sender, System.currentTimeMillis());

                // If the user does not have permission to execute the sub command, don't let
                // them execute and return permission denied
                if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
                    exitCode = ExitCode.EXIT_PERMISSION_DENIED;
                } else if (this.getRequiresPlayer() && !(sender instanceof ProxiedPlayer)) {
                    exitCode = ExitCode.EXIT_MUST_BE_PLAYER;
                } else {
                    exitCode = executor.apply(sender, a, variables);
                }
            }
        } catch (Exception e) {
            exitCode = ExitCode.EXIT_ERROR;
            e.printStackTrace();
        }

        if (exitCode != ExitCode.EXIT_SUCCESS) {
            if (exitCode == ExitCode.EXIT_INFO) {
                _playSound(sender, NotificationType.INFO);
                return;
            }
            errorHandler.apply(exitCode, sender, a, variables);
            _playSound(sender, NotificationType.ERROR);
        } else {
            _playSound(sender, NotificationType.SUCCESS);
        }
    }

    /**
     * Set the executor of the command
     * 
     * @param executor to set
     * @return {@link BungeeCommandBuilder}
     */
    public BungeeCommandBuilder onExecute(@NotNull Executor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Set the tab complete executor of the command
     * 
     * @param executor to set
     * @return {@link BungeeCommandBuilder}
     */
    public BungeeCommandBuilder onTabComplete(@NotNull TabExecutor executor) {
        this.tabExecutor = executor;
        return this;
    }

    /**
     * Set the error handler of the command
     * 
     * @param handler to set
     * @return {@link BungeeCommandBuilder}
     */
    public BungeeCommandBuilder onError(@NotNull ErrorHandler handler) {
        this.errorHandler = handler;
        return this;
    }

    /**
     * Build this BungeeCord command
     * 
     * @param plugin to register register with
     * 
     * @return {@link Command}
     */
    public Command build(Plugin plugin) {
        return new TabableCommand(this);
    }

    /**
     * Register the command with a {@link net.md_5.bungee.api.plugin.Plugin}
     * 
     * @param plugin to register with
     */
    public void register(Plugin plugin) {
        Command command = build(plugin);
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, command);
    }

    private static class TabableCommand extends net.md_5.bungee.api.plugin.Command
            implements net.md_5.bungee.api.plugin.TabExecutor {
        BungeeCommandBuilder builder;

        public TabableCommand(BungeeCommandBuilder builder) {
            super(builder.getName(), builder.getPermission(), builder.getAliases().toArray(new String[0]));
            this.builder = builder;
        }

        public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
            // CommandSender sender, CommandBuilder builder, String label, List<String> args
            if (builder.getSynchronous()) {
                builder.performExecution(sender, builder, builder.getName(), Arrays.asList(args));
            } else {
                builder.performAsynchronousExecution(sender, builder, builder.getName(), Arrays.asList(args));
            }
        }

        @Override
        public Iterable<String> onTabComplete(net.md_5.bungee.api.CommandSender sender, String[] args) {
            if (builder.tabExecutor != null)
                return builder.tabExecutor.apply(sender, builder.getName(), new Arguments(Arrays.asList(args)));
            else {
                if (args.length == 0) {
                    return ImmutableList.of();
                }

                String lastWord = args[args.length - 1];

                ProxiedPlayer senderPlayer = sender instanceof ProxiedPlayer ? (ProxiedPlayer) sender : null;

                ArrayList<String> matchedPlayers = new ArrayList<String>();
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    String name = player.getName();
                    if ((senderPlayer == null) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                        matchedPlayers.add(name);
                    }
                }

                Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
                return matchedPlayers;
            }
        }
    }

    private void _playSound(CommandSender sender, NotificationType type) {
        if (!this.getPlaySound())
            return;
        SoundUtil.send(sender, type);
    }

}
