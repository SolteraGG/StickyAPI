/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

/**
 * Enum based exit codes for StickyAPI command classes.
 */
public enum ExitCode {
    /**
     * If the command executed successfully
     */
    EXIT_SUCCESS,
    /**
     * If the command did not execute successfully due to an unexpected error
     */
    EXIT_ERROR,
    /**
     * If the sender provided invalid syntax
     */
    EXIT_INVALID_SYNTAX,
    /**
     * If the sender did not have permission to execute the command
     */
    EXIT_PERMISSION_DENIED,
    /**
     * If the sender is of an invalid type (prefer EXIT_MUST_BE_PLAYER when possible)
     */
    EXIT_BAD_SENDER,
    /**
     * If the sender specifically must be a player
     */
    EXIT_MUST_BE_PLAYER,
    /**
     * If the sender provided valid syntax but is not in a valid state (e.g. running a sell command while empty-handed)
     */
    EXIT_INVALID_STATE,
    /**
     * If the command has a cooldown period and the sender is performing the command too fast
     */
    EXIT_COOLDOWN,
    /**
     * If the command did not execute successfully, but the error was expected and handled cleanly
     */
    EXIT_EXPECTED_ERROR,
    /**
     * If the command failed, but the command will handle the error message itself. Although there is no difference
     * between EXIT_SUCCESS and EXIT_ERROR_SILENT, prefer using this exit code when possible for clearer code
     */
    EXIT_ERROR_SILENT;
}

/*
public enum ExitCode {

    EXIT_SUCCESS,

    EXIT_ERROR,

    EXIT_INVALID_SYNTAX("The syntax you have provided is invalid, please check the command you entered!"),

    EXIT_PERMISSION_DENIED("You do not have permission to perform this command!"),

    EXIT_BAD_SENDER("You cannot perform this command as this console or entity!"),

    EXIT_MUST_BE_PLAYER("You must perform this command as a player!"),

    EXIT_INVALID_STATE("You cannot perform this command in this state!"),

    EXIT_COOLDOWN("Please wait before running this command again."),

    EXIT_EXPECTED_ERROR("The command could not be performed."),

    EXIT_ERROR_SILENT(null);
}

*/