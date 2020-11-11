/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.util;

import java.util.List;

import com.dumbdogdiner.stickyapi.common.util.ReflectionUtil;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

/**
 * This class is meant to help with Bukkit and Koffee commands
 */
public class CommandUtil {
    /**
     * Register a command with bukkit
     * @param server The bukkit server
     * @param command The command to register
     * @return False if something went wrong
     */
    public static boolean registerCommand(Server server, Command command) {
        try {
            CommandMap cmap = ReflectionUtil.getProtectedValue(server, "commandMap");
            cmap.register(server.getName().toLowerCase(), command);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Register a {@link java.util.List} of commands with bukkit
     * @param server The bukkit server
     * @param commands The command to register
     * @return False if something went wrong
     */
    public static boolean registerCommands(Server server, List<Command> commands) {
        try {
            CommandMap cmap = ReflectionUtil.getProtectedValue(server, "commandMap");
            cmap.registerAll(server.getName().toLowerCase(), commands);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Register all commands in a specific package
     * 
     * @param server The bukkit server
     * @param pkg    The package path (e.g. com.ristexsoftware.knappy.commands)
     * @return False if something went horribly wrong
     * @throws ClassNotFoundException If the comamnd class cannot be located
     * @throws IllegalAccessException if the command class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
     * @throws InstantiationException If we are unable to instaniate the command class
     * @deprecated This method uses reflection and is prone to error in later Java
     *             versions
     */
    @Deprecated
    public static boolean registerCommandPackage(Server server, String pkg)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<String> classNames;
        try (ScanResult scanResult = new ClassGraph().acceptPackages(pkg).enableClassInfo().scan()) {
            classNames = scanResult.getAllClasses().getNames();
        }
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            Object command = clazz.newInstance(); //FIXME: java.lang.InstantiationException
            CommandMap cmap = ReflectionUtil.getProtectedValue(server, "commandMap");
            cmap.register(server.getName().toLowerCase(), (Command)command);
        }
        return true;
    }
}
