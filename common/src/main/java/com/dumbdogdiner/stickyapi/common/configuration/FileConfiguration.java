/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Interface for File-based Configurations.
 */
public interface FileConfiguration extends Configuration {

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
