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
 * @version 2.0.0
 */
public class StickyAPI {
    @Getter
    public static Logger logger = Logger.getLogger("StickyAPI");

    @Getter
    @Setter
    private static ExecutorService pool = Executors.newCachedThreadPool();

    // Build Info Start
    private static final String dateFormat = "@BUILDINFO_DATEFORMAT@";
    // Custom Getter
    private static final String timestamp = "@BUILDINFO_TIMESTAMP@";
    @Getter
    private static final String commit = "@BUILDINFO_COMMIT@";
    @Getter
    private static final String branch = "@BUILDINFO_BRANCH@";
    // Custom Getter
    private static final String isDirty = "@BUILDINFO_ISDIRTY@"; 

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

    public static String getSha() {
        return commit.substring(0, 7);
    }

    public static Boolean getIsDirty() {
        return Boolean.parseBoolean(isDirty);
    }
}
