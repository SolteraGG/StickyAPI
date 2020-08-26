package com.ristexsoftware.knappy.bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public interface SenderTest extends CommandSender {
    /**
     * Test whether this sender has the given permission, specifying if console is allowed.
     */
    public default Boolean hasPermission(String permission, Boolean allowConsole) {
        return !(allowConsole || this instanceof ConsoleCommandSender) && this.hasPermission(permission);
    }    
}
