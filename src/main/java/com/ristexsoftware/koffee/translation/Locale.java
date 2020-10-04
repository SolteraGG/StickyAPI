/* 
 *  Koffee - A simple collection of utilities I commonly use
 *  Copyright (C) 2019-2020 Justin Crawford <justin@Stacksmash.net>
 *  Copyright (C) 2019-2020 Zachery Coleman <Zachery@Stacksmash.net>
 *  Copyright (C) 2019-2020 Skye Elliot <actuallyori@gmail.com>
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

package com.ristexsoftware.koffee.translation;

import java.io.File;

import com.ristexsoftware.koffee.configuration.file.FileConfiguration;
import com.ristexsoftware.koffee.configuration.file.YamlConfiguration;
import com.ristexsoftware.koffee.util.Debugger;

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
     * @param localeFile The locale file to use
     * @return The new locale object
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
     * @param The node to get
     * @return The node if it exists
     */
    public String get(String node) {
        debug.reset().print("fetching node " + node);
        debug.print("node value: " + localeConfig.getString(node));

        return localeConfig.getString(node);
    }

}