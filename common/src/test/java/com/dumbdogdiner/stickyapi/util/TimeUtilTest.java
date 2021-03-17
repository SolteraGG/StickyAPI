/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;

import java.util.Optional;

public class TimeUtilTest {

    @Test
    public void testDurationStringOneEverything() {
        Timestamp ts = new Timestamp(32144461000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 year, 1 week, 1 day, 1 hour, 1 minute, and 1 second");
    }

    @Test
    public void testDurationStringEverything() {
        Timestamp ts = new Timestamp(65077567000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "2 years, 3 weeks, 4 days, 5 hours, 6 minutes, and 7 seconds");
    }

    @Test
    public void testDurationStringOneSecond() {
        Timestamp ts = new Timestamp(1000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 second");
    }

    @Test
    public void testDurationStringTwoSeconds() {
        Timestamp ts = new Timestamp(2000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "2 seconds");
    }

    @Test
    public void testDurationStringOneMinute() {
        Timestamp ts = new Timestamp(60000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 minute");
    }

    @Test
    public void testDurationStringOneHour() {
        Timestamp ts = new Timestamp(3600000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 hour");
    }

    @Test
    public void testDurationStringOneDay() {
        Timestamp ts = new Timestamp(86400000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 day");
    }

    @Test
    public void testDurationStringOneWeek() {
        Timestamp ts = new Timestamp(604800000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 week");
    }

    @Test
    public void testDurationStringOneYear() {
        Timestamp ts = new Timestamp(31449600000L); // duration in millis
        assertEquals(TimeUtil.durationString(ts), "1 year");
    }

    @Test
    public void testDurationLongOneSecond() {
        assertEquals(TimeUtil.duration("1s"), Optional.of(1L));
    }

    @Test
    public void testDurationLongUnknownMultiplier() {
        assertEquals(TimeUtil.duration("1z"), Optional.empty());
    }

    @Test
    public void testSignificantDurationString() {
        String time = TimeUtil.significantDurationString(6000000L);
        assertEquals(time, "1.67/h");
    }

    @Test
    public void testExpiresZero() {
        Timestamp ts = new Timestamp(0L);
        assertEquals(TimeUtil.expirationTime(ts), "never expires");
    }
}
