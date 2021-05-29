/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class AveragesTest {
	@Test
	void testMean() {
		// primitive lists
		int[] primitiveIntDataset = {1, 2, 3};
		long[] primitiveLongDataset = {1, 2, 3};
		double[] primitiveDoubleDataset = {1, 2, 3};
		Assertions.assertEquals(2, Averages.getMean(primitiveIntDataset));
		Assertions.assertEquals(2, Averages.getMean(primitiveLongDataset));
		Assertions.assertEquals(2, Averages.getMean(primitiveDoubleDataset));
		// boxed list
		List<Number> dataset = Arrays.stream(primitiveDoubleDataset).boxed().collect(Collectors.toList());
		Assertions.assertEquals(2, Averages.getMean(dataset));
	}

	@Test
	void testMedian() {
		// primitive lists
		int[] primitiveIntDataset = {1, 2, 3};
		long[] primitiveLongDataset = {1, 2, 3};
		double[] primitiveDoubleDataset = {1, 2, 3};
		Assertions.assertEquals(2, Averages.getMedian(primitiveIntDataset));
		Assertions.assertEquals(2, Averages.getMedian(primitiveLongDataset));
		Assertions.assertEquals(2, Averages.getMedian(primitiveDoubleDataset));
		// boxed list
		List<Number> dataset = Arrays.stream(primitiveDoubleDataset).boxed().collect(Collectors.toList());
		Assertions.assertEquals(2, Averages.getMedian(dataset));

		// intermediate values
		primitiveIntDataset = new int[]{1, 2, 3, 4};
		primitiveLongDataset = new long[]{1, 2, 3, 4};
		primitiveDoubleDataset = new double[]{1, 2, 3, 4};
		Assertions.assertEquals(2.5, Averages.getMedian(primitiveIntDataset));
		Assertions.assertEquals(2.5, Averages.getMedian(primitiveLongDataset));
		Assertions.assertEquals(2.5, Averages.getMedian(primitiveDoubleDataset));

		dataset = Arrays.stream(primitiveDoubleDataset).boxed().collect(Collectors.toList());
		Assertions.assertEquals(2.5, Averages.getMedian(dataset));
	}
}
