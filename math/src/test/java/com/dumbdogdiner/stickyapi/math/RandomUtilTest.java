/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;;

// TODO: Random element tests
class RandomUtilTest {
	@RepeatedTest(100)
	void testMaxRandomInt() {
		Assertions.assertTrue(RandomUtil.randomInt(5) < 5);
	}

	@RepeatedTest(100)
	void testRangedRandomInt() {
		int value = RandomUtil.randomInt(3, 7);
		Assertions.assertTrue(value >= 3 && value <= 7);
	}

	@RepeatedTest(100)
	void testMaxRandomDouble() {
		Assertions.assertTrue(RandomUtil.randomDouble(5) < 5);
	}
}
