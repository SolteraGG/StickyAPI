package com.ristexsoftware.koffee.bukkit.util;

import java.util.List;

import com.ristexsoftware.koffee.util.ReflectionUtil;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

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
     * @param server The bukkit server
     * @param pkg The package path (e.g. com.ristexsoftware.knappy.commands)
     * @return False if something went horribly wrong
     * @deprecated This method uses reflection and is prone to error in later Java versions
     */
    public static boolean registerCommandPackage(Server server, String pkg) {
        List<String> classNames;
        try (ScanResult scanResult = new ClassGraph().acceptPackages(pkg).enableClassInfo().scan()) {
            classNames = scanResult.getAllClasses().getNames();
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                Object command = clazz.newInstance();
                CommandMap cmap = ReflectionUtil.getProtectedValue(server, "commandMap");
                cmap.register(server.getName().toLowerCase(), (Command)command);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
