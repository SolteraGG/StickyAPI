/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import lombok.Getter;

public class ExitMessage {
    /**
     * The message to send to the command sender in case of an error, or null if a
     * message should not be sent
     */
    @Getter
    private String message;
    
    ExitMessage(String message) {
        this.message = message;
    }

    ExitMessage() {
        this.message = null;
    }

    // Allow this message to be defined by the user!!!
    // This allows us to not have hard-coded exit messages, for example
    // if I want to provide an example of the syntax when I return
    // EXIT_INVALID_SYNTAX
    // Returning the ExitMessage allows for something like
    // return ExitCode.success.setMessage("yay!");
    /**
     * Set the message of an {@link ExitCode}
     * 
     * @param message
     * @return {@link ExitCode}
     */
    public ExitMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}