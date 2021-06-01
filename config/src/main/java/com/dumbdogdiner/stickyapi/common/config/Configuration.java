/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.config;

/**
 * Base interface for a configuration.
 */
public interface Configuration {
    /**
     * Gets the given string from the path, returning the given default value if not found.
     * @param path The path of the string to get
     * @param def The default value if the path is not found or is not a string
     * @return The requested string.
     */
    public String getString(String path, String def);
    
    /**
     * Gets the given string from the path.
     * @param path The path of the string to get
     * @return The requested string, or null.
     */
    public String getString(String path);

    /**
     * Gets the given double from the path.
     * @param path The path of the double to get
     * @return The requested double, or null.
     */
    public Double getDouble(String path);

    /**
     * Gets the given double from the path, returning the given default value if not found.
     * @param path The path of the double to get
     * @param def The default value if the path is not found or is not a double
     * @return The requested double.
     */
    public double getDouble(String path, double def);

    /**
     * Gets the given integer from the path.
     * @param path The path of the integer to get
     * @return The requested integer, or null.
     */
    public Integer getInt(String path);

    /**
     * Gets the given integer from the path, returning the given default value if not found.
     * @param path The path of the integer to get
     * @param def The default value if the path is not found or is not an integer
     * @return The requested integer.
     */
    public int getInt(String path, int def);


}
