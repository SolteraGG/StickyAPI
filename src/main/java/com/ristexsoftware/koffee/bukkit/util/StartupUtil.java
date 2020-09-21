package com.ristexsoftware.koffee.bukkit.util;

import org.bukkit.plugin.java.JavaPlugin;

public class StartupUtil {

    /**
     * Generate a configuration datafolder and save the default config if one does not exist
     * @param plugin The plugin's main class
     * @return False if something went wrong
     */
    public boolean setupConfig(JavaPlugin plugin) {
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

    
    
}
