/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.configuration;

import java.io.IOException;

/**
 * Interface for File-based Configurations.
 */
public interface FileConfiguration extends Configuration {

    /**
     * Save the configuration to the given location.
     */
    public void save(String path) throws IOException;
}
