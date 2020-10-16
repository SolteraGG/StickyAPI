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
     * @return {@link com.dumbdogdiner.stickyapi.common.translation.Locale}
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