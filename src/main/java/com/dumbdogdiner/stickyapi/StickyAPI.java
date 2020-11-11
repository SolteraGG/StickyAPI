/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * a thing that exists
 */
public class StickyAPI {
    @Getter
    public static Logger logger = Logger.getLogger("koffee");

    @Getter
    @Setter
    private static ExecutorService pool = Executors.newFixedThreadPool(3);
}