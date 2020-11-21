/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * <h1>StickyAPI</h1>
 * Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins.
 * @author DumbDogDiner <dumbdogdiner.com>
 * @version 2.0.0
 */
public class StickyAPI {
    @Getter
    public static Logger logger = Logger.getLogger("StickyAPI");

    @Getter
    @Setter
    private static ExecutorService pool = Executors.newCachedThreadPool();
}
