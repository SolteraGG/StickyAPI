/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.stats;

import com.dumbdogdiner.stickyapi.math.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DistributionTest {
	static final double[] DATASET_X = {1, 2, 3, 4, 5, 6, 7, 8, 9};
	static final double[] DATASET_Y = {9, 8, 7, 6, 5, 4, 3, 2, 1};

	@Test
	void testExpectation() {
		Assertions.assertEquals(5, Distribution.expectation(DATASET_X));
		Assertions.assertEquals(5, Distribution.expectation(DATASET_Y));
	}

	@Test
	void testVariance() {
		Assertions.assertEquals(6.666666666666668, Distribution.variance(DATASET_X));
		Assertions.assertEquals(6.666666666666668, Distribution.variance(DATASET_Y));
	}

	@Test
	void testStandardDeviation() {
		Assertions.assertEquals(2.5819888974716116, Distribution.standardDeviation(DATASET_X));
		Assertions.assertEquals(2.5819888974716116, Distribution.standardDeviation(DATASET_Y));
	}

	@Test
	void testPCC() {
		Assertions.assertTrue(NumberUtil.almostEquals(-1, Distribution.pcc(DATASET_X, DATASET_Y)));
	}

	@Test
	void testIsAssociated() {
		Assertions.assertTrue(NumberUtil.almostEquals(-1, Distribution.pcc(DATASET_X, DATASET_Y)));
		Assertions.assertTrue(Distribution.isAssociated(DATASET_X, DATASET_Y));
	}
}
