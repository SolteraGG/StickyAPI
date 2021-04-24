/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrdinalTest {
	@Test()
	void testFirst() {
		Assertions.assertEquals("st", Ordinal.getOrdinal(1));
		// mod 10
		Assertions.assertEquals("st", Ordinal.getOrdinal(21));
		// mod 100
		Assertions.assertEquals("st", Ordinal.getOrdinal(101));
	}

	@Test()
	void testSecond() {
		Assertions.assertEquals("nd", Ordinal.getOrdinal(2));
		// mod 10
		Assertions.assertEquals("nd", Ordinal.getOrdinal(22));
		// mod 100
		Assertions.assertEquals("nd", Ordinal.getOrdinal(102));
	}

	@Test()
	void testThird() {
		Assertions.assertEquals("rd", Ordinal.getOrdinal(3));
		// mod 10
		Assertions.assertEquals("rd", Ordinal.getOrdinal(23));
		// mod 100
		Assertions.assertEquals("rd", Ordinal.getOrdinal(103));
	}

	@Test()
	void testTeenException() {
		Assertions.assertEquals("th", Ordinal.getOrdinal(11));
		Assertions.assertEquals("th", Ordinal.getOrdinal(12));
		Assertions.assertEquals("th", Ordinal.getOrdinal(13));
		// mod 100
		Assertions.assertEquals("th", Ordinal.getOrdinal(111));
	}

	@Test()
	void testGeneral() {
		Assertions.assertEquals("th", Ordinal.getOrdinal(4));
		// mod 10
		Assertions.assertEquals("th", Ordinal.getOrdinal(14));
		// mod 100
		Assertions.assertEquals("th", Ordinal.getOrdinal(104));
	}
}
