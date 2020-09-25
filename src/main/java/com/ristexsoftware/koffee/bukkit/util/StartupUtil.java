package com.ristexsoftware.koffee.bukkit.util;

import java.io.File;
import com.ristexsoftware.koffee.translation.LocaleProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class StartupUtil {

    /**
     * Generate a configuration datafolder and save the default config if one does
     * not exist
     * 
     * @param plugin The plugin's main class
     * @return False if something went wrong
     */
    public static boolean setupConfig(JavaPlugin plugin) {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getLogger().info("Error: No folder was found! Creating...");
                plugin.getDataFolder().mkdirs();
                plugin.saveDefaultConfig();
                plugin.saveConfig();
                plugin.getLogger().info("The folder was created successfully!");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load the server's localizations
     * 
     * @param plugin         The plugin's main class
     * @param localeProvider The plugin's locale provider
     * @return False if something went wrong
     */
    public static LocaleProvider setupLocale(JavaPlugin plugin, LocaleProvider localeProvider) {
        localeProvider = new LocaleProvider(new File(plugin.getDataFolder(), "locale"));
        int loadedLocales = localeProvider.loadAllLocales();
        Boolean localeEnabled = localeProvider.setDefaultLocale("messages.en_us");

        if (!localeEnabled) {
            plugin.getLogger()
                    .severe("Failed to configure default locale file - perhaps you deleted it? Will create a new one.");
            // FIXME: This is horrible and needs to be improved
            try {
                localeProvider.writeLocaleStream(plugin.getResource("messages.en_us.yml"), "messages.en_us.yml", true);
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("Something went horribly wrong while saving the default locale.");
                return null;
            }

            localeProvider.loadAllLocales();
            localeProvider.setDefaultLocale("messages.en_us");
        } else
            plugin.getLogger().info("Loaded " + String.valueOf(loadedLocales) + " localizations");

        return localeProvider;
    }
}
