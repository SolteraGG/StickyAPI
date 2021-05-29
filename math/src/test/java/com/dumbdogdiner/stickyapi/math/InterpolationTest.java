/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InterpolationTest {
	@Test
	void testUnclampedLerp() {
		// test lerp 0 => 1, t = 0.5
		Assertions.assertEquals(0.5, Interpolation.lerpUnclamped(0, 1, 0.5));
		// test lerp 0 => 2, t = 0.5
		Assertions.assertEquals(1.0, Interpolation.lerpUnclamped(0, 2, 0.5));
		// test lerp 0 => 1, t = 2
		Assertions.assertEquals(2.0, Interpolation.lerpUnclamped(0, 1, 2));
	}

	@Test
	void testClamp() {
		// test clamp 0 => 1, t = 0.5
		Assertions.assertEquals(0.5, Interpolation.clamp(0, 1, 0.5));
		// test clamp 0 => 2, t = 0.5
		Assertions.assertEquals(0.5, Interpolation.clamp(0, 2, 0.5));
		// test clamp 0 => 1, t = 2
		Assertions.assertEquals(1.0, Interpolation.clamp(0, 1, 2));
	}

	@Test
	void testLerp() {
		// test clamp 0 => 1, t = 0.5
		Assertions.assertEquals(0.5, Interpolation.lerp(0, 1, 0.5));
		// test clamp 0 => 2, t = 0.5
		Assertions.assertEquals(1.0, Interpolation.lerp(0, 2, 0.5));
		// test clamp 0 => 1, t = 2
		Assertions.assertEquals(1.0, Interpolation.lerp(0, 1, 2));
	}
}
