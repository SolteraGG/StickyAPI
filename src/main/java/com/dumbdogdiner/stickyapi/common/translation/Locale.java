/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.translation;

import com.dumbdogdiner.stickyapi.common.configuration.file.FileConfiguration;
import com.dumbdogdiner.stickyapi.common.configuration.file.YamlConfiguration;
import com.dumbdogdiner.stickyapi.common.util.Debugger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents a wrapper around a locale configuration file.
 */
public class Locale {
    private final Debugger debug = new Debugger(getClass());

    @Getter
    @NotNull
    Boolean isValid = false;

    @Getter
    File localeFile;

    @Getter
    @NotNull
    FileConfiguration localeConfig = new YamlConfiguration();

    /**
     * Create a new locale object
     * <p>
     * Returns the new locale object
     * 
     * @param localeFile The locale file to use
     */
    public Locale(@NotNull File localeFile) {
        this.localeFile = localeFile;
        try {
            localeConfig.load(this.localeFile);
            isValid = true;
        } catch (Exception e) {
            e.printStackTrace();
            isValid = false;
        }
    }

    /**
     * Get a locale value.
     * <p>
     * Returns the node if it exists
     * 
     * @param node The node to get
     * @return {@link java.lang.String}
     */
    public @Nullable String get(@NotNull String node) {
        debug.reset().print("fetching node " + node);
        debug.print("node value: " + localeConfig.getString(node));

        return localeConfig.getString(node);
    }

}
