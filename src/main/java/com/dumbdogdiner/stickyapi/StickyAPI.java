/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * <h1>StickyAPI</h1> Utility methods, classes and potentially
 * code-dupe-annihilating code for DDD plugins.
 * 
 * @author DumbDogDiner (dumbdogdiner.com)
 * @version 2.0.0
 */
public class StickyAPI {
    @Getter
    public static Logger logger = Logger.getLogger("StickyAPI");

    @Getter
    @Setter
    private static @NotNull ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * Provides a wrapper for {@link java.lang.Class#getResourceAsStream(String)} (String)}
     * @param resourceName The resource to get
     * @return an {@link InputStream} to that resource
     */
    public static InputStream getResourceAsStream(@NotNull String resourceName){
        return StickyAPI.class.getResourceAsStream(resourceName);
    }

    private StickyAPI() {}
}
