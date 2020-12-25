/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import lombok.Getter;

/**
 * Enum based exit codes for StickyAPI command classes.
 * 
 * @deprecated use {@link com.dumbdogdiner.stickyapi.common.command.ExitCode} as
 *             this will be removed in the next release
 * @see {@link com.dumbdogdiner.stickyapi.common.command.ExitCode}
 */
@Deprecated
public enum ExitCode {
    /**
     * If the command executed successfully
     */
    EXIT_SUCCESS(null),
    /**
     * If the command did not execute successfully due to an unexpected error
     */
    EXIT_ERROR(
            "There was an internal server error while attempting to perform this command, ask the server administrator to read the console"),
    /**
     * If the sender provided invalid syntax
     */
    EXIT_INVALID_SYNTAX("The syntax you have provided is invalid, please check the command you entered!"),
    /**
     * If the sender did not have permission to execute the command
     */
    EXIT_PERMISSION_DENIED("You do not have permission to perform this command!"),
    /**
     * If the sender is of an invalid type (prefer EXIT_MUST_BE_PLAYER when
     * possible)
     */
    EXIT_BAD_SENDER("You cannot perform this command as this console or entity!"),
    /**
     * If the sender specifically must be a player
     */
    EXIT_MUST_BE_PLAYER("You must perform this command as a player!"),
    /**
     * If the sender provided valid syntax but is not in a valid state (e.g. running
     * a sell command while empty-handed)
     */
    EXIT_INVALID_STATE("You cannot perform this command in this state!"),
    /**
     * If the command has a cooldown period and the sender is performing the command
     * too fast
     */
    EXIT_COOLDOWN("Please wait before running this command again."),
    /**
     * If the command did not execute successfully, but the error was expected and
     * handled cleanly
     */
    EXIT_EXPECTED_ERROR("The command could not be performed."),
    /**
     * If the command failed, but the command will handle the error message itself.
     * Although there is no difference between EXIT_SUCCESS and EXIT_ERROR_SILENT,
     * prefer using this exit code when possible for clearer code
     */
    EXIT_ERROR_SILENT(null);

    /**
     * The message to send to the command sender in case of an error, or null if a
     * message should not be sent
     */
    @Getter
    private String message;

    ExitCode(String message) {
        this.message = message;
    }

    // Allow this message to be defined by the user!!!
    // This allows us to not have hard-coded exit messages, for example
    // if I want to provide an example of the syntax when I return
    // EXIT_INVALID_SYNTAX
    // Returning the ExitCode allows for something like
    // return ExitCode.EXIT_SUCCESS.setMessage("yay!");
    /**
     * Set the message of an {@link ExitCode}
     * 
     * @param message
     * @return {@link ExitCode}
     */
    public ExitCode setMessage(String message) {
        this.message = message;
        return this;
    }
}
