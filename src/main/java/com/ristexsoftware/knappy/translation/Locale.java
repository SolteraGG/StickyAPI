/* 
 *  Knappy - A simple collection of utilities I commonly use
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

package com.ristexsoftware.knappy.translation;

import java.io.File;

import com.ristexsoftware.knappy.configuration.file.FileConfiguration;
import com.ristexsoftware.knappy.configuration.file.YamlConfiguration;

import lombok.Getter;

/**
 * Represents a wrapper around a locale configuration file.
 */
public class Locale {

    @Getter
    Boolean isValid = false;

    @Getter
    File localeFile;

    @Getter
    FileConfiguration localeConfig = new YamlConfiguration();

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
     */
    public String get(String node) {
        return localeConfig.getString(node);
    }

}