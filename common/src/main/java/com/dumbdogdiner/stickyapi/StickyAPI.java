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
public class StickyAPI {
    private StickyAPI() {
    }

    @Getter
    public static Logger logger = Logger.getLogger("StickyAPI");

    @Getter
    @Setter
    private static ExecutorService pool = Executors.newCachedThreadPool();

    // Build Info Start (OLD)

    /**
     * Get the current version of API.
     * @deprecated Use {@link BuildInfo#getVersion()}
     * @since 3.0
     * @return {@link String} version
     */
    @Getter
    @Deprecated
    private static final String version = BuildInfo.getVersion().toString();

    /**
     * Get a string with the latest commit (id) at API's build-time.
     * @deprecated Use {@link BuildInfo#getCommit()}
     * @since 3.0
     * @return {@link String} commit id
     */
    @Getter
    @Deprecated
    private static final String commit = BuildInfo.getCommit();

    /**
     * Get a string with the current branch at API's build-time.
     * @deprecated Use {@link BuildInfo#getBranch()}     *
     * @since 3.0
     * @return {@link String} branch name
     */
    @Getter
    @Deprecated
    private static final String branch = BuildInfo.getBranch();

    /**
     * Get a boolean showing if the working tree was dirty at API's build-time.
     * <p>
     * If the working directory was dirty, this will return true, meaning there were modified tracked files and staged changes at build-time.
     * @deprecated Use {@link BuildInfo#getBranch()}
     * @since 3.0
     * @return {@link Boolean} isDirty
     */
    @Deprecated
    @Getter
    private static final boolean isDirty = BuildInfo.isDirty();


    /**
     * Get a string with the latest commit sha at API's build-time.
     * @deprecated This method does not work the same way Github's does, this can be fixed by contributing to the versioning plugin and then addign to {@link BuildInfo}
     * @since 3.0
     * @return {@link String} sha
     */
    @Deprecated
    public static String getSha() {
        return commit.substring(0, 7);
    }
}
