/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector3Test {
	@Test
	void testVector3Equal() {
		Assertions.assertEquals(new Vector3(1, 2, 3), new Vector3(1, 2, 3));
	}

	@Test
	void testVector3Add() {
		Vector3 a = new Vector3(0, 1, 2);
		Vector3 b = new Vector3(1, 1, 1);
		Assertions.assertEquals(new Vector3(1, 2, 3), a.add(b));
	}

	@Test
	void testVector3Subtract() {
		Vector3 a = new Vector3(0, 1, 2);
		Vector3 b = new Vector3(1, 1, 1);
		Assertions.assertEquals(new Vector3(-1, 0, 1), a.subtract(b));
	}

	@Test
	void testVector3Scale() {
		Vector3 a = new Vector3(10, 5, 20);
		// check unitary scale
		Assertions.assertEquals(a, a.scale(1));
		// check 2 scale
		Assertions.assertEquals(new Vector3(20, 10, 40), a.scale(2));
		// check negative scale
		Assertions.assertEquals(new Vector3(-10, -5, -20), a.scale(-1));
	}

	@Test
	void testVector3To() {
		Vector3 a = new Vector3(0, 1, 2);
		Vector3 b = new Vector3(1, 1, 1);
		// TODO: skye do some math and work out what this should be you lazy hoe
		Assertions.assertEquals(new Vector3(1, 0, -1), a.to(b));
	}
}
