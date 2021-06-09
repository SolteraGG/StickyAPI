/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.config;

import java.io.File;
import java.io.FileWriter;

/**
 * Interface for File-based Configurations.
 */
public interface FileConfiguration extends Configuration {

    /**
     * Reload the current configuration, for example to load new data.
     * This will reload the configuration file from the filesystem into memory,
     * overwriting the currently loaded data.
     */
    public void reload();

    /**
     * Save the configuration to the given string path.
     * 
     * @param path {@link String} output filename path
     * @return Returns a {@link Boolean} value depending on if the save was successful or not
     */
    public boolean save(String path);

    /**
     * 
     * @param output {@link File} to output to
     * @return Returns a {@link Boolean} value depending on if the save was successful or not
     */
    public boolean save(File output);

    /**
     * Save the configuration to the given FileWriter.
     * 
     * @param output {@link FileWriter} to output to
     */
    public void save(FileWriter output);
}
