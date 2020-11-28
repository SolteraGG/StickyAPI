/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import com.dumbdogdiner.stickyapi.common.command.ExitCode;
import org.jetbrains.annotations.Nullable;

/**
 * An enum of possible notification types.
 */
public enum NotificationType {
    ERROR,
    INFO,
    QUIET,
    SUCCESS;

    public static NotificationType fromExitCode(@Nullable ExitCode code){
        if(code == null)
            return QUIET;
        switch(code){
            case EXIT_SUCCESS:
                return SUCCESS;
            case EXIT_INFO:
                return INFO;
            case EXIT_BAD_SENDER:
            case EXIT_COOLDOWN:
            case EXIT_ERROR:
            case EXIT_ERROR_SILENT:
            case EXIT_EXPECTED_ERROR:
            case EXIT_INVALID_STATE:
            case EXIT_INVALID_SYNTAX:
            case EXIT_MUST_BE_PLAYER:
            case EXIT_PERMISSION_DENIED:
            default:
                return ERROR;
        }
    }
}
