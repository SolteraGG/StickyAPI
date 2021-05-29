/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.transform;

import com.dumbdogdiner.stickyapi.math.NumberUtil;
import com.dumbdogdiner.stickyapi.math.vector.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransformationBuilder2Test {
	@Test
	void testSimpleTransformation() {
		// enlargement s.f. 2
		TransformationBuilder2 builder = new TransformationBuilder2().apply(Matrix2.getEnlargementMatrix(2));
		Assertions.assertEquals(new Vector2(2, -4), builder.transform(new Vector2(1, -2)));
	}

	@Test
	void testRepeatedTransformation() {
		// enlargement s.f. 2, then rotate pi radians
		TransformationBuilder2 builder = new TransformationBuilder2()
			.apply(Matrix2.getEnlargementMatrix(2))
			.apply(Matrix2.getEnlargementMatrix(2));
		Assertions.assertEquals(new Vector2(8, -16), builder.transform(new Vector2(2, -4)));
	}

	@Test
	void testInverseTransformation() {
		// enlargement s.f. 2, then back again
		TransformationBuilder2 builder = new TransformationBuilder2()
			.apply(Matrix2.getEnlargementMatrix(2))
			.apply(Matrix2.getEnlargementMatrix(0.5));
		Vector2 a = new Vector2(2, -4);
		Assertions.assertEquals(a, builder.transform(a));
	}

	@Test
	void testRotationTransformation() {
		TransformationBuilder2 builder = new TransformationBuilder2()
			.apply(Matrix2.getRotationMatrix(Math.PI));
		// translate vectors
		Vector2 original = new Vector2(1, 1);
		Vector2 translated = builder.transform(original);
		// due to floating point inaccuracy
		Assertions.assertTrue(NumberUtil.almostEquals(-1, translated.getX()));
		Assertions.assertTrue(NumberUtil.almostEquals(-1, translated.getY()));
	}

	@Test
	void testCompositeTransformation() {
		TransformationBuilder2 builder = new TransformationBuilder2()
			.apply(Matrix2.getEnlargementMatrix(2))
			.apply(Matrix2.getRotationMatrix(Math.PI));
		// translate vectors
		Vector2 original = new Vector2(1, 1);
		Vector2 translated = builder.transform(original);
		// due to floating point inaccuracy
		Assertions.assertTrue(NumberUtil.almostEquals(-2, translated.getX()));
		Assertions.assertTrue(NumberUtil.almostEquals(-2, translated.getY()));
	}
}
