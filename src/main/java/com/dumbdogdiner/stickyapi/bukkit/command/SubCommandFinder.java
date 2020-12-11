/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.command;

import com.dumbdogdiner.stickyapi.StickyAPI;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*internal*/ final class SubCommandFinder {
    private SubCommandFinder() {}

    /**
     * Add subcommands to a plugin based on its annotations.
     * @param command The command to scan and modify.
     */
    @SuppressWarnings("unchecked cast")
    public static void addSubCommands(@NotNull StickyPluginCommand command) {
        try {
            // find all inherited classes, in case a command extends another
            var classes = new ArrayList<Class<? extends StickyPluginCommand>>();
            Class<?> temp = command.getClass();
            while (StickyPluginCommand.class.isAssignableFrom(temp)) {
                classes.add((Class<? extends StickyPluginCommand>) temp);
                temp = temp.getSuperclass();
            }
            // reverse so we go in order of extension, not inheritance
            Collections.reverse(classes);
            for (var clazz : classes) {
                for (var field : clazz.getFields()) {
                    // find a @SubCommand field
                    var annotation = (SubCommand) Arrays.stream(field.getAnnotations())
                            .filter(a -> a instanceof SubCommand)
                            .findFirst()
                            .orElse(null);

                    // if none exists, move on
                    if (annotation == null) continue;

                    // class.path.and.ClassName#fieldName
                    var fieldPath = clazz.getName() + "#" + field.getName();

                    // must be static: the order of initialization says that supers call before the subclass has a
                    // chance to set any instance variables, so static variables will always be null at this state.
                    if (!Modifier.isStatic(field.getModifiers())) {
                        StickyAPI.getLogger().severe(
                                "The @SubCommand annotation is incompatible with non-static field " + fieldPath);
                        continue;
                    }

                    // annotated field must be an instance of StickyPluginCommand (obviously)
                    var fieldClass = field.getType();
                    if (!(StickyPluginCommand.class.isAssignableFrom(fieldClass))) {
                        StickyAPI.getLogger().severe(
                                "The @SubCommand annotation is incompatible with the type of " + fieldPath);
                        continue;
                    }

                    // subcommand must not be null
                    var subCommand = (StickyPluginCommand) field.get(null);
                    if (subCommand == null) {
                        StickyAPI.getLogger().severe(
                                "The field " + fieldPath + " was null when trying to add it as a subcommand");
                        continue;
                    }

                    // add subcommand by its name and aliases
                    command.getSubCommands().put(subCommand.getName(), subCommand);
                    for (var alias : subCommand.getAliases()) {
                        command.getSubCommands().put(alias, subCommand);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            // oopsie woopsie uwu
            e.printStackTrace();
        }
    }
}
