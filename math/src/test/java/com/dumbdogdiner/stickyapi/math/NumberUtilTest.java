/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberUtilTest {
	@Test
	void testIsSignedInteger() {
		// truthy cases
		Assertions.assertTrue(NumberUtil.isInteger("123"));
		Assertions.assertTrue(NumberUtil.isInteger("-123"));
		// falsey cases
		Assertions.assertFalse(NumberUtil.isInteger(""));
		Assertions.assertFalse(NumberUtil.isInteger("  "));
		Assertions.assertFalse(NumberUtil.isInteger("12 3"));
		Assertions.assertFalse(NumberUtil.isInteger("ab2c"));
		Assertions.assertFalse(NumberUtil.isInteger("12-3"));
		Assertions.assertFalse(NumberUtil.isInteger("12.3"));
	}

	@Test
	void testIsUnsignedInteger() {
		// truthy cases
		Assertions.assertTrue(NumberUtil.isInteger("123", false));
		// falsey cases
		Assertions.assertFalse(NumberUtil.isInteger("-123", false));
		Assertions.assertFalse(NumberUtil.isInteger("", false));
		Assertions.assertFalse(NumberUtil.isInteger("  ", false));
		Assertions.assertFalse(NumberUtil.isInteger("12 3", false));
		Assertions.assertFalse(NumberUtil.isInteger("ab2c", false));
		Assertions.assertFalse(NumberUtil.isInteger("12-3", false));
		Assertions.assertFalse(NumberUtil.isInteger("12.3", false));
	}

	@Test
	void testIsNumeric() {
		// truthy cases
		Assertions.assertTrue(NumberUtil.isNumeric("123"));
		Assertions.assertTrue(NumberUtil.isNumeric("-123"));
		Assertions.assertTrue(NumberUtil.isNumeric("-123.123"));
		Assertions.assertTrue(NumberUtil.isNumeric("-123e123"));
		Assertions.assertTrue(NumberUtil.isNumeric("-123e-123"));
		Assertions.assertTrue(NumberUtil.isNumeric("-123.123e-123"));
		// falsey cases
		Assertions.assertFalse(NumberUtil.isInteger("", false));
		Assertions.assertFalse(NumberUtil.isInteger("  ", false));
		Assertions.assertFalse(NumberUtil.isInteger("12 3", false));
		Assertions.assertFalse(NumberUtil.isInteger("ab2c", false));
		Assertions.assertFalse(NumberUtil.isInteger("12-3", false));
	}

	@Test
	void testIntHelper() {
		Assertions.assertEquals(NumberUtil.longToInt(1L), 1);
	}

	@Test
	void testIntHelperMax() {
		Assertions.assertEquals(NumberUtil.longToInt(((long)Integer.MAX_VALUE) + 1L), Integer.MAX_VALUE);
	}

	@Test
	void testIntHelperMin() {
		Assertions.assertEquals(NumberUtil.longToInt(((long) Integer.MIN_VALUE) - 1L), Integer.MIN_VALUE);
	}
}
