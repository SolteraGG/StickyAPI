/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.util;

import com.dumbdogdiner.stickyapi.Enums;
import org.junit.jupiter.api.Test;

public class NotificationTypeTest {

    @Test
    public void enumNotificationType() {
        Enums.superficialEnumCodeCoverage(NotificationType.class);
    }
}
