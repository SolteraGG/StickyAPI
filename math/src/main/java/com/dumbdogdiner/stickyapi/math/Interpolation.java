/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import com.google.common.base.Preconditions;

/**
 * Utility class for interpolating between values.
 * TODO: Inaccuracy issues for longs.
 */
public final class Interpolation {
	private Interpolation() {}

	/**
	 * Clamp the value `t` between `min` and `max`.
	 * @param max The minimum value
	 * @param min The maximum value
	 * @param t The value to clamp
	 * @return The clamped value between `max` and `min`.
	 */
	public static double clamp(Number min, Number max, Number t) {
		Preconditions.checkNotNull(min);
		Preconditions.checkNotNull(max);
		Preconditions.checkNotNull(t);
		return Double.min(max.doubleValue(), Double.max(min.doubleValue(), t.doubleValue()));
	}

	/**
	 * Linearly interpolate between a and b with time parameter t.
	 * `t` is clamped between `0` and `1`.
	 * @param a The first value
	 * @param b The second value
	 * @param t The time parameter
	 * @return The linearly interpolated value between `a` and `b`.
	 */
	public static double lerp(Number a, Number b, Number t) {
		return lerpUnclamped(a, b, clamp(0, 1, t));
	}

	/**s
	 * Linearly interpolate between a and b with time parameter t.
	 * `t` is not clamped between `0` and `1`.
	 * @param a The first value
	 * @param b The second value
	 * @param t The time parameter
	 * @return The linearly interpolated value.
	 */
	public static double lerpUnclamped(Number a, Number b, Number t) {
		Preconditions.checkNotNull(a);
		Preconditions.checkNotNull(b);
		Preconditions.checkNotNull(t);
		return b.doubleValue() * t.doubleValue() + (1 - t.doubleValue()) * a.doubleValue();
	}

	/**
	 * Sinusoidaly interpolate between <code>a</code> and <code>b</code> with time parameter <code>t</code>.
	 * @param a The first value
	 * @param b The second value
	 * @param t The time parameter
	 * @return The sinusoidaly interpolated value.
	 */
	public static double sinusoidal(Number a, Number b, Number t) {
		// calculate sinusoidal param
		double param = Math.pow(Math.sin(t.doubleValue() * Math.PI / 2), 2);
		return lerp(a, b, param);
	}
}
