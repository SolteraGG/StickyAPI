package com.dumbdogdiner.stickyapi.bukkit.util;

import static com.dumbdogdiner.stickyapi_tests_common.TestsCommon.superficialEnumCodeCoverage;

import org.junit.jupiter.api.Test;

public class NotificationTypeTest {
    @Test
    public void enumNotificationType() {
        superficialEnumCodeCoverage(NotificationType.class);
    }
}
