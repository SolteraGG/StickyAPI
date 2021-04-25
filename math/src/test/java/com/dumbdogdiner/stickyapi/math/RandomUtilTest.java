/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;;

// TODO: Random element tests
class RandomUtilTest {
	@RepeatedTest(100)
	void testMaxRandomInt() {
		// this method is inclusive, so test for inclusivity.
		Assertions.assertTrue(RandomUtil.randomInt(5) <= 5);
	}

	@RepeatedTest(100)
	void testMaxRandomIntExclusive() {
		// this method is exclusive, so test for exclusivity.
		Assertions.assertTrue(RandomUtil.randomIntExclusive(5) < 5);
	}

	@Test
	void testRandomIntNonPositiveArgument() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RandomUtil.randomInt(-1);
		});
	}

	@RepeatedTest(100)
	void testRangedRandomInt() {
		// this method is inclusive, so test for inclusivity.
		int value = RandomUtil.randomInt(3, 7);
		Assertions.assertTrue(value >= 3 && value <= 7);
	}

	@Test
	void testRangedRandomIntIllegalRange() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RandomUtil.randomInt(1, 0);
		});
	}

	@RepeatedTest(100)
	void testRangedRandomIntNegativeRange() {
		Assertions.assertDoesNotThrow(() -> {
			RandomUtil.randomInt(-5, 5);
		});
	}

	@RepeatedTest(100)
	void testMaxRandomDouble() {
		// this method is exclusive, so test for exclusivity.
		Assertions.assertTrue(RandomUtil.randomDouble(5) < 5);
	}

	@RepeatedTest(100)
	void testRangedRandomDouble() {
		// this method is inclusive, so test for inclusivity.
		double value = RandomUtil.randomDouble(3, 7);
		Assertions.assertTrue(value >= 3 && value <= 7);
	}

	@RepeatedTest(100)
	void testRandomAngle() {
		double value = RandomUtil.randomAngle();
		Assertions.assertTrue(value >= 0 && value <= Math.PI * 2);
	}

	@RepeatedTest(100)
	void testRandomDualAngle() {
		double value = RandomUtil.randomDualAngle();
		Assertions.assertTrue(value >= -Math.PI && value <= Math.PI);
	}

	@RepeatedTest(100)
	void testRandomVector2() {
		Assertions.assertTrue(NumberUtil.almostEquals(RandomUtil.randomVector2().abs(), 1));
		Assertions.assertTrue(NumberUtil.almostEquals(RandomUtil.randomVector2(2).abs(), 2));
	}

	@RepeatedTest(100)
	void testRandomVector3() {
		System.out.println(RandomUtil.randomVector3().abs());
		Assertions.assertTrue(NumberUtil.almostEquals(RandomUtil.randomVector3().abs(), 1));
		Assertions.assertTrue(NumberUtil.almostEquals(RandomUtil.randomVector3(2).abs(), 2));
	}
}
