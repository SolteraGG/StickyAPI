/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector2Test {
	@Test
	void testVector2Equal() {
		Assertions.assertEquals(new Vector2(1, 2), new Vector2(1, 2));
	}

	@Test
	void testVector2Add() {
		Vector2 a = new Vector2(0, 1);
		Vector2 b = new Vector2(1, 1);
		Assertions.assertEquals(new Vector2(1, 2), a.add(b));
	}

	@Test
	void testVector2Subtract() {
		Vector2 a = new Vector2(0, 1);
		Vector2 b = new Vector2(1, 1);
		Assertions.assertEquals(new Vector2(-1, 0), a.subtract(b));
	}

	@Test
	void testVector2Scale() {
		Vector2 a = new Vector2(10, 5);
		// check unitary scale
		Assertions.assertEquals(a, a.scale(1));
		// check 2 scale
		Assertions.assertEquals(new Vector2(20, 10), a.scale(2));
		// check negative scale
		Assertions.assertEquals(new Vector2(-10, -5), a.scale(-1));
	}

	@Test
	void testVector2To() {
		Vector2 a = new Vector2(0, 1);
		Vector2 b = new Vector2(1, 0);
		Assertions.assertEquals(new Vector2(1, -1), a.to(b));
	}
}
