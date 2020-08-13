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
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.ristexsoftware.knappy.Knappy;
import com.ristexsoftware.knappy.configuration.file.FileConfiguration;
import com.ristexsoftware.knappy.configuration.file.YamlConfiguration;
import com.ristexsoftware.knappy.translation.Translation;

import lombok.Getter;

public class Locale {

    @Getter
    FileConfiguration config;
    
    public Locale(FileConfiguration config) {
        this.config = config;
    }


    

    // private static Knappy self = Knappy.getKnappy();
    // private static File customConfigFile;
    // private static FileConfiguration customConfig;
    // private static Locale localself = null;

    // // Everything else
    // public static String prefix;
    // public static String networkName;
    // public static String website;
    // public static String serverError;
    // public static String invalidSyntax;
    // public static boolean discord;

    // // Initialized by our GetMessages() function.
    // protected Locale() {
    //     String translationFile = self.getConfig().getString("general.translation-file", "messages.en_us.yml");
    //     customConfigFile = new File(self.getDataFolder(), translationFile);
    //     if (!customConfigFile.exists()) {
    //         customConfigFile.getParentFile().mkdirs();
    //         self.saveResource(translationFile, false);
    //     }

    //     reload();
    // }

    // /**
    //  * Get the messages object associated with messages.yml
    //  * 
    //  * @return A Messages object referencing the messages.yml file.
    //  */
    // public static Locale getMessages() {
    //     if (Locale.localself == null)
    //         Locale.localself = new Locale();
    //     return Locale.localself;
    // }

    // /**
    //  * Reload the messages.yml file and update the internal configuration values.
    //  */
    // public static void reload() {
    //     FileConfiguration fc = new YamlConfiguration();
    //     try {
    //         fc.load(customConfigFile);
    //         customConfig = fc;

    //         // Messages
    //         Locale.prefix = customConfig.getString("prefix", "[knappy] ").replace("&", "\u00A7");
    //         Locale.networkName = Locale._translate(customConfig.getString("network-name", "My Network"), new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER));
    //         Locale.website = Locale._translate(customConfig.getString("website", "YourWebsiteHere.com"), new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER));
    //         Locale.serverError = Locale._translate(customConfig.getString("server-error", "The server encountered an error!"), new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER));
    //         Locale.invalidSyntax = Locale._translate(customConfig.getString("invalid-syntax", "&cInvalid Syntax!"), new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER));
    //         Locale.discord = self.getConfig().getBoolean("discord.enabled", false);
    //     } 
    //     catch (IOException | InvalidConfigurationException e) 
    //     {
    //         e.printStackTrace();
    //     }
    // }

    // /**
    //  * Get the configuration object for messages.yml
    //  * @return {@link org.bukkit.configuration.file.FileConfiguration}
    //  */
    // public FileConfiguration getConfig()
    // {
    //     return customConfig;
    // }

    // private static String _translate(String configMessage, Map<String, String> vars)
    // {
    //     if (configMessage == null)
    //     return null;
        
    //     vars.put("prefix", Locale.prefix);
    //     vars.put("networkname", Locale.networkName);
    //     vars.put("website", Locale.website);

    //     return Translation.translate(configMessage, "&", vars);
    // }
    
    // private static String _translateNoColor(String configMessage, Map<String, String> vars)
    // {
    //     if (configMessage == null)
    //         return null;
        
    //     vars.put("prefix", Locale.prefix);
    //     vars.put("networkname", Locale.networkName);
    //     vars.put("website", Locale.website);

    //     return Translation.translateVariables(configMessage, vars);
    // }

    // /**
    //  * Lookup the string from messages.yml, replace the placeholders and convert the color codes into colors
    //  * then return the resulting string for sending to a minecraft client.
    //  * @param configNode The config node for the message in messages.yml
    //  * @param vars Variables to use for placeholders
    //  * @return A string to send to the minecraft client with all colors and placeholders converted.
    //  * @throws InvalidConfigurationException if the message node does not exist in messages.yml
    //  */
    // public static String translate(String configNode, Map<String, String> vars) throws InvalidConfigurationException
    // {
    //     getMessages();
    //     String configMessage = Locale.customConfig.getString(configNode);
    //     if (configMessage == null)
    //     throw new InvalidConfigurationException("Configuration Node is invalid or does not exist: " + configNode);
        
    //     return _translate(configMessage, vars);
    // }

    // /**
    //  * Lookup a string from the messages.yml file and replace the placeholders with the variables but do not 
    //  * convert the sequences which may be interpreted as color characters.
    //  * @param configNode the node from messages.yml to lookup for the string
    //  * @param vars Placeholders to replace in the string looked up from messages.yml
    //  * @return A string with all the placeholders converted
    //  * @throws InvalidConfigurationException If the messages.yml node does not exist
    //  */
    // public static String translateNc(String configNode, Map<String, String> vars) throws InvalidConfigurationException
    // {
    //     getMessages();
    //     String configMessage = Locale.customConfig.getString(configNode);
    //     if (configMessage == null)
    //         throw new InvalidConfigurationException("Configuration Node is invalid or does not exist: " + configNode);

    //     return _translateNoColor(configMessage, vars);
    // }

    // /**
    //  * Check if many strings equal a single comparison string
    //  * @param haystack the string to compare to
    //  * @param needles things that may match the comparison string
    //  * @return Whether something matches.
    //  */
    // public static boolean compareMany(String haystack, String[] needles)
    // {
    //     for (String needle : needles)
    //     {
    //         if (haystack.equalsIgnoreCase(needle))
    //             return true;
    //     }
        
    //     return false;
    // }
}