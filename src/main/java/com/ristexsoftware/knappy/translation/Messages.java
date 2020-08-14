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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ristexsoftware.knappy.configuration.file.FileConfiguration;
import com.ristexsoftware.knappy.configuration.file.YamlConfiguration;

public class Messages {

    public static enum LanguageCode {
        en_US,
        en_UK,
        de_DE,
        en_ES,
        CUSTOM;
    }

    private static Map<LanguageCode, FileConfiguration> languages = new HashMap<LanguageCode, FileConfiguration>();

    //FIXME: add proper description 

    /**
     * For LanguageCode detection, language files should match the following pattern: <code>file-name.[ISO LANG CODE].yml</code>
     * @param folderPath The absolute folder path for your langauge files.
     */
    public Messages(File folderPath) throws IOException {
        if(!folderPath.isDirectory())
            throw new IOException("The specified file is not a directory.");

        File languageFolder = folderPath;

        if(!languageFolder.exists()) {
            languageFolder.mkdirs();
        }

        for(File file : languageFolder.listFiles()) {
            String[] splitName = file.getName().split(".");
            if(splitName.length < 3) continue;

            LanguageCode langCode = LanguageCode.valueOf(splitName[1]) != null ? LanguageCode.valueOf(splitName[1]) : LanguageCode.CUSTOM; 
            YamlConfiguration fileConf = new YamlConfiguration();
            try {
                fileConf.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileConfiguration fc = fileConf;

            if(fc == null) 
                continue;

            languages.put(langCode, fc);

        }
    }

}