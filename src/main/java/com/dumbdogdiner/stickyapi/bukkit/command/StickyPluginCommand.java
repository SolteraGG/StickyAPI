/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import com.dumbdogdiner.stickyapi.bukkit.command.builder.CommandBuilder;
import com.dumbdogdiner.stickyapi.bukkit.plugin.StickyPlugin;
import com.dumbdogdiner.stickyapi.bukkit.util.SoundUtil;
import com.dumbdogdiner.stickyapi.common.ServerVersion;
import com.dumbdogdiner.stickyapi.common.arguments.Arguments;
import com.dumbdogdiner.stickyapi.common.command.ExitCode;
import com.dumbdogdiner.stickyapi.common.translation.LocaleProvider;
import com.dumbdogdiner.stickyapi.common.util.NotificationType;
import com.dumbdogdiner.stickyapi.common.util.ReflectionUtil;
import com.dumbdogdiner.stickyapi.common.util.TimeUtil;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.dumbdogdiner.stickyapi.common.command.ExitCode.*;


/**
 * Represents a {@link org.bukkit.command.Command} belonging to a plugin
 * <p>
 * Cloned from bukkit to prevent reflective calls
 */
// Fuck you reflection, and fuck you Java for changing it so much!!!
//I'ma fuggin rewrite bits of this so its not garbage
public abstract class StickyPluginCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    @Getter
    private final StickyPlugin owningPlugin;
    @Getter
    protected List<Permission> commandPermissions = new ArrayList<>();
    protected boolean playSounds = false;
    @Getter
    private final LocaleProvider locale;
    protected long COOLDOWN_TIME;
    protected CooldownManager cooldowns;
    @Getter
    protected Permission basePermission;
    protected boolean requiresPlayer = false;


    /**
     * A StickyPluginCommand
     *
     * @param name    the name of the command
     * @param aliases aliases of the command
     * @param owner   the owning plugin
     */
    public StickyPluginCommand(@NotNull String name, @Nullable List<String> aliases, @NotNull StickyPlugin owner) {
        this(name, aliases, owner, new Permission(CommandBuilder.getBasePermissionName(owner, name)));
    }

    /**
     * A StickyPluginCommand
     *
     * @param name           the name of the command
     * @param aliases        aliases of the command
     * @param owner          the owning plugin
     * @param basePermission the permission to execute the command
     */
    public StickyPluginCommand(@NotNull String name, @Nullable List<String> aliases, @NotNull StickyPlugin owner, @NotNull Permission basePermission) {
        super(name);
        if (aliases != null)
            setAliases(aliases);
        this.owningPlugin = owner;
        this.basePermission = basePermission;
        setPermission(basePermission.getName());
        this.locale = owner.getLocale();
        commandPermissions.add(0, basePermission);
        cooldowns = new CooldownManager(COOLDOWN_TIME);
    }

    /**
     * A StickyPluginCommand
     *
     * @param name           the name of the command
     * @param aliases        aliases of the command
     * @param owner          the owning plugin
     * @param basePermission the permission to execute the command
     */
    public StickyPluginCommand(@NotNull String name, @Nullable List<String> aliases, @NotNull StickyPlugin owner, @NotNull Permission basePermission, boolean playSounds) {
        this(name, aliases, owner, basePermission);
        this.playSounds = playSounds;
    }


    abstract public ExitCode execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull Arguments args, @NotNull Map<String, String> variables);

    /**
     * Executes the command, returning its success, is final so as to encourage use of StickyAPI arguments, etc
     * <p>
     * Returns true if the command was successful, otherwise false
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return {@link java.lang.Boolean}
     */
    @Override
    final public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!owningPlugin.isEnabled()) {
            throw new CommandException("Cannot execute command '" + commandLabel + "' in plugin "
                    + owningPlugin.getDescription().getFullName() + " - plugin is disabled.");
        }

        Map<String, String> variables = LocaleProvider.newVariables();
        variables.put("command", getName());
        variables.put("sender", sender.getName());
        variables.put("player", sender.getName());
        variables.put("uuid", (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "");
        Arguments arguments = new Arguments(Arrays.asList(args));

        long cooldown = cooldowns.getSenderRemainingCooldown(sender);
        if (cooldown > 0L) {
            variables.put("cooldown", TimeUtil.durationString(COOLDOWN_TIME));
            variables.put("cooldown-remaining", TimeUtil.durationString(cooldown));
            onError(sender, commandLabel, arguments, ExitCode.EXIT_COOLDOWN, variables);
            return true;
        }

        if (requiresPlayer && sender instanceof ConsoleCommandSender) {
            onError(sender, commandLabel, arguments, EXIT_MUST_BE_PLAYER, variables);
            return true;
        }

        if (sender instanceof Player && !sender.hasPermission(basePermission)) {
            onError(sender, commandLabel, arguments, EXIT_PERMISSION_DENIED, variables);
            return true;
        }

        try {
            ExitCode code = execute(sender, commandLabel, arguments, variables);
            onError(sender, commandLabel, arguments, code, variables);
            return true;
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin "
                    + owningPlugin.getDescription().getFullName(), ex);

        }
    }

    public void onError(CommandSender sender, String commandLabel, Arguments arguments, ExitCode code, Map<String, String> vars) {
        if (code == null)
            return;
        playSound(sender, code);
        switch (code) {
            case EXIT_ERROR_SILENT:
            case EXIT_SUCCESS:
            case EXIT_INFO:
                return;
            case EXIT_PERMISSION_DENIED:
                sender.sendMessage(locale.translate("permission-denied", vars));
                return;
            case EXIT_BAD_SENDER:
                sender.sendMessage(locale.translate("bad-sender", vars));
                return;
            case EXIT_MUST_BE_PLAYER:
                sender.sendMessage(locale.translate("must-be-player", vars));
                return;
            case EXIT_COOLDOWN:
                sender.sendMessage(locale.translate("cooldown", vars));
                return;
            case EXIT_INVALID_SYNTAX:
                if (usageMessage.length() > 0) {
                    for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                        sender.sendMessage(line);
                    }
                }
                return;
            case EXIT_ERROR:
            default:
                sender.sendMessage(locale.translate("generic-error", vars));
        }
    }


    /**
     * Gets the owner of this PluginCommand
     *
     * @return Plugin that owns this command
     */
    @Override
    @NotNull
    public Plugin getPlugin() {
        return getOwningPlugin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException, CommandException;

    /**
     * <p>
     * Stub method to perform validation for tabcomplete
     * <p>
     * If it is not present or returns null, will delegate to the current command
     * executor if it implements {@link TabCompleter}. If a non-null list has not
     * been found, will default to standard player name completion in
     * {@link StickyPluginCommand#tabComplete(CommandSender, String, String[])}.
     * <p>
     *
     * @throws IllegalArgumentException if sender, alias, or args is null
     */

    public void validateTabComplete(@NotNull CommandSender sender, @NotNull String alias,
                                    @NotNull String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(", ").append(owningPlugin.getDescription().getFullName()).append(')');
        return stringBuilder.toString();
    }

    /**
     * Register the command the owner {@link org.bukkit.plugin.Plugin}
     */
    public final void register() {

        // If the server is running paper, we don't need to do reflection, which is
        // good.
        if (ServerVersion.isPaper()) {
            owningPlugin.getServer().getCommandMap().register(owningPlugin.getName(), this);
            return;
        }
        // However, if it's not running paper, we need to use reflection, which is
        // really annoying
        ((CommandMap) Objects.requireNonNull(ReflectionUtil.getProtectedValue(owningPlugin.getServer(), "commandMap"))).register(owningPlugin.getName(), this);
    }

    private void playSound(CommandSender sender, ExitCode code) {
        playSound(sender, NotificationType.fromExitCode(code));
    }

    private void playSound(CommandSender sender, NotificationType type) {
        if (this.playSounds)
            SoundUtil.send(sender, type);
    }

    void enableSounds() {
        playSounds = true;
    }
}
