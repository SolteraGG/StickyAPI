/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.mockedplugin;


import com.dumbdogdiner.stickyapi.bukkit.util.StartupUtil;
import com.dumbdogdiner.stickyapi.common.translation.LocaleProvider;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class StickyAPIPlugin extends JavaPlugin {
    @Getter
    private LocaleProvider localeProvider;
    public StickyAPIPlugin() {
        super();
    }

    protected StickyAPIPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        if (!StartupUtil.setupConfig(this))
            return;

        this.localeProvider = StartupUtil.setupLocale(this, this.localeProvider);
        if (this.localeProvider == null)
            return;

        // Do more stuff??
        getLogger().info("StickyAPI Dummy Plugin started successfully!");
    }
}
