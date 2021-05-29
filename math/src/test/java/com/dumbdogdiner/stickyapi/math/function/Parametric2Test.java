/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.function;

import com.dumbdogdiner.stickyapi.math.vector.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Parametric2Test {
	@Test
	void testParametric2() {
		Parametric2 curve = new Parametric2(
			(t) -> Math.pow(t,2),
			(t) -> Math.pow(t, 2) - 3 * t
		);
		Assertions.assertEquals(new Vector2(9, 0), curve.evaluate(3));
	}
}
