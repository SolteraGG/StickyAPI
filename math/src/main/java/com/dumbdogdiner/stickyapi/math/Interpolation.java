/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math;

import com.google.common.base.Preconditions;

/**
 * Utility class for interpolating between values.
 */
public final class Interpolation {
	private Interpolation() {}

	/**
	 * Represents a cubic bezier curve.
	 */
	public class CubicBezier {
		private double a;
		private double b;
		private double c;
		private double d;

		/**
		 * Construct a new cubic bezier with the specified parameters
		 * @param a The first parameter
		 * @param b The second parameter
		 * @param c The third parameter
		 * @param d The fourth parameter
		 */
		public CubicBezier(double a, double b, double c, double d) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}

		/**
		 * Evaluate this cubic bezier for time t.
		 * @param t The time parameter
		 * @return A value based on the cubic bezier.
		 */
		public double evaluate(Number t) {
			return t.doubleValue();
		}
	}

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
}
