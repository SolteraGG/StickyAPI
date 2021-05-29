/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.function;

import com.dumbdogdiner.stickyapi.math.vector.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a parametric curve.
 */
public class Parametric2 {
	final Function<Double, Double> x;
	final Function<Double, Double> y;

	/**
	 * Construct a new parametric curve from the specified functions.
	 * @param x The function describing the <code>x</code> co-ordinate
	 * @param y The function describing the <code>y</code> co-ordinate
	 */
	public Parametric2(
		Function<@NotNull Double, @NotNull Double> x,
		Function<@NotNull Double, @NotNull Double> y
	) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Evaluate this parametric curve at time <code>t</code>.
	 * @param t The time parameter
	 * @return A {@link Vector2} representing the parametric output.
	 */
	public Vector2 evaluate(double t) {
		return new Vector2(
			this.x.apply(t),
			this.y.apply(t)
		);
	}
}
