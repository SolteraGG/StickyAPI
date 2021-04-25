package com.dumbdogdiner.stickyapi.math.function;

import com.dumbdogdiner.stickyapi.math.vector.Vector2;

/**
 * Represents a cubic bezier curve.
 */
public class CubicBezier {
	private final Vector2 a;
	private final Vector2 b;
	private final Vector2 c;
	private final Vector2 d;

	/**
	 * Construct a new cubic bezier with the specified parameters
	 * @param a The first parameter
	 * @param b The second parameter
	 * @param c The third parameter
	 * @param d The fourth parameter
	 */
	public CubicBezier(double a, double b, double c, double d) {
		this(new Vector2(a, b), new Vector2(c, d));
	}

	/**
	 * Construct a new cubic bezier with the specified vector parameters.
	 * @param b The first vector
	 * @param c The second vector
	 */
	public CubicBezier(Vector2 b, Vector2 c) {
		this(Vector2.zero(), b, c, Vector2.one());
	}

	/**
	 * Construct a raw cubic bezier with the specified vector parameters.
	 * @param a The first vector
	 * @param b The second vector
	 * @param c The third vector
	 * @param d The fourth vector
	 */
	public CubicBezier(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	/**
	 * Evaluate this cubic bezier for time <code>val</code>.
	 * @param val The time parameter
	 * @return A value based on the cubic bezier.
	 */
	public Vector2 evaluateToVector(Number val) {
		double t = val.doubleValue();
		double q = 1 - t;

		// B(t) = (1 - t)^3P0 + (1-t)^2*tP1 etc.
		return this.a.scale(Math.pow(q, 3)).add(
			this.b.scale(3 * Math.pow(q, 2) * t)
		).add(
			this.c.scale(3 * q * Math.pow(t, 2))
		).add(
			this.d.scale(Math.pow(t, 3))
		);
	}
}
