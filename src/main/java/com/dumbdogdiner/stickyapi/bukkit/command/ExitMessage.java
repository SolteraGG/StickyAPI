/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
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