/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.translation;

import java.io.File;

import com.dumbdogdiner.stickyapi.common.configuration.file.FileConfiguration;
import com.dumbdogdiner.stickyapi.common.configuration.file.YamlConfiguration;
import com.dumbdogdiner.stickyapi.common.util.Debugger;

import lombok.Getter;

/**
 * Represents a wrapper around a locale configuration file.
 */
public class Locale {
    private Debugger debug = new Debugger(getClass());

    @Getter
    Boolean isValid = false;

    @Getter
    File localeFile;

    @Getter
    FileConfiguration localeConfig = new YamlConfiguration();

    /**
     * Create a new locale object
     * <p>Returns the new locale object
     * @param localeFile The locale file to use
     */
    public Locale(File localeFile) {
        this.localeFile = localeFile;
        try {
            localeConfig.load(this.localeFile);
            isValid = true;
        } catch(Exception e) {
            e.printStackTrace();
            isValid = false;
        }
    }

    /**
     * Get a locale value.
     * <p>Returns the node if it exists
     * @param node The node to get
     * @return {@link java.lang.String}
     */
    public String get(String node) {
        debug.reset().print("fetching node " + node);
        debug.print("node value: " + localeConfig.getString(node));

        return localeConfig.getString(node);
    }

}