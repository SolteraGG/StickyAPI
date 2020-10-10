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
     * If the command did not execute successfully due to an error
     */
    EXIT_ERROR,
    /**
     * If the sender provided invalid syntax
     */
    EXIT_INVALID_SYNTAX,
    /**
     * If the sender did not have permission to execute the command
     */
    EXIT_PERMISSION_DENIED;
}
