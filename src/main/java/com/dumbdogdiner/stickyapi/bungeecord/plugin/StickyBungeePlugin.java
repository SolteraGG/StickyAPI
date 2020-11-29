/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.plugin;

import com.dumbdogdiner.stickyapi.common.translation.LocaleProvider;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

abstract public class StickyBungeePlugin extends Plugin {
    @Getter
    protected LocaleProvider locale;

    /**
     * {@inheritDoc}
     * Plugin initial load logic
     */
    @Override
    public abstract void onLoad();

    /**
     * {@inheritDoc}
     * Plugin startup logic
     */
    @Override
    public abstract void onEnable();


    /**
     * {@inheritDoc}
     * Plugin shutdown logic
     */
    @Override
    public abstract void onDisable();
}
