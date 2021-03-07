/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * <h1>StickyAPI</h1> Utility methods, classes and potentially
 * code-dupe-annihilating code for DDD plugins.
 * 
 * @author DumbDogDiner (dumbdogdiner.com)
 */
@SuppressWarnings("ConstantConditions")
public class StickyAPI {
    private StickyAPI() {
    }

    @Getter
    public static Logger logger = Logger.getLogger("StickyAPI");

    @Getter
    @Setter
    private static ExecutorService pool = Executors.newCachedThreadPool();

    // Build Info Start

    /**
     * Get the current version of API.
     * 
     * @since TBA
     * @return {@link String} version
     * 
     */
    @Getter
    private static final String version = "@BUILDINFO_VERSION@";

    // Getter not required
    private static final String dateFormat = "@BUILDINFO_DATEFORMAT@";

    // Custom Getter (see below)
    private static final String timestamp = "@BUILDINFO_TIMESTAMP@";

    /**
     * Get a string with the latest commit (id) at API's build-time.
     * 
     * @since TBA
     * @return {@link String} commit id
     */
    @Getter
    private static final String commit = "@BUILDINFO_COMMIT@";

    /**
     * Get a string with the current branch at API's build-time.
     * 
     * @since TBA
     * @return {@link String} branch name
     */
    @Getter
    private static final String branch = "@BUILDINFO_BRANCH@";

    // Custom Getter (see below)
    private static final String isDirty = "@BUILDINFO_ISDIRTY@";

    
    /**
     * Get a Date object showing the current time at API's build-time.
     * 
     * @since TBA
     * @return {@link Date} date
     */
    public static Date getTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            Date date = formatter.parse(timestamp);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a string with the latest commit sha at API's build-time.
     * 
     * @since TBA
     * @return {@link String} sha
     */
    public static String getSha() {
        return commit.substring(0, 7);
    }

    /**
     * Get a boolean showing if the working tree was dirty at API's build-time.
     * <p>
     * If the working directory was dirty, this will return true, meaning there were modified tracked files and staged changes at build-time.
     * 
     * @since TBA
     * @return {@link Boolean} isDirty
     */
    public static Boolean getIsDirty() {
        return Boolean.parseBoolean(isDirty);
    }
}
