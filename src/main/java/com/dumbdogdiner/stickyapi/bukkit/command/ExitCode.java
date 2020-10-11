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
