/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.transform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Matrix2Test {
	@Test
	void testMatrix2Equals() {
		Matrix2 a = Matrix2.getIdentityMatrix();
		Assertions.assertEquals(new Matrix2(1, 0, 0 ,1), a);
	}

	@Test
	void testMatrix2Identity() {
		Matrix2 a = new Matrix2(1, 2, 3, 4);
		Assertions.assertEquals(a, a.preMultiply(Matrix2.getIdentityMatrix()));
		Assertions.assertEquals(a, a.postMultiply(Matrix2.getIdentityMatrix()));
	}

	@Test
	void testMatrix2Add() {
		Matrix2 a = new Matrix2(1, 2, 3, 4);
		Matrix2 b = new Matrix2(4, 3, 2, 1);
		Assertions.assertEquals(new Matrix2(5, 5, 5, 5), a.add(b));
	}

	@Test
	void testMatrix2Subtract() {
		Matrix2 a = new Matrix2(4, 8, 7, 4);
		Matrix2 b = new Matrix2(5, 4, 6, 5);
		Assertions.assertEquals(new Matrix2(-1, 4, 1, -1), a.subtract(b));
	}

	@Test
	void testMatrix2Scale() {
		Matrix2 a = new Matrix2(1, 7, -6, 2);
		Assertions.assertEquals(a, a.scale(1));
		Assertions.assertEquals(new Matrix2(2, 14, -12, 4), a.scale(2));
		Assertions.assertEquals(new Matrix2(-1, -7, 6, -2), a.scale(-1));
	}

	@Test
	void testMatrixPreMultiply() {
		Matrix2 a = new Matrix2(1, 2, 3, 4);
		Matrix2 b = new Matrix2(4, 3, 2, 1);
		Assertions.assertEquals(new Matrix2(13, 20, 5, 8), a.preMultiply(b));
	}

	@Test
	void testMatrixPostMultiply() {
		Matrix2 a = new Matrix2(1, 2, 3, 4);
		Matrix2 b = new Matrix2(4, 3, 2, 1);
		Assertions.assertEquals(new Matrix2(8, 5, 20, 13), a.postMultiply(b));
	}
}
