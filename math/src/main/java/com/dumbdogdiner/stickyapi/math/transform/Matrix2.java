/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.transform;

import lombok.Getter;

/**
 * Represents a 2x2 mathematical, immutable matrix. The matrix has the following layout:
 * <pre>
 * (a b)
 * (c d)
 * </pre>
 */
public class Matrix2 {
	/**
	 * Generate an anticlockwise rotation matrix for the target angle.
	 * @param theta The target angle in radians
	 * @return A {@link Matrix2} representing a rotation <code>theta</code> radians about the origin.
	 */
	public static Matrix2 getRotationMatrix(double theta) {
		return new Matrix2(Math.cos(theta), -Math.sin(theta), Math.sin(theta), Math.cos(theta));
	}

	/**
	 * Generate an enlargement matrix for the target scale factor.
	 * @param k The target scale factor
	 * @return A {@link Matrix2} representing an enlargement scale factor <code>k</code> centered at the origin.
	 */
	public static Matrix2 getEnlargementMatrix(double k) {
		return new Matrix2(k, 0, 0, k);
	}

	/**
	 * Generate a new identity matrix.
	 * @return A {@link Matrix2} representing an identity matrix.
	 */
	public static Matrix2 getIdentityMatrix() {
		return getEnlargementMatrix(1);
	}

	@Getter
	private final double a;
	@Getter
	private final double b;
	@Getter
	private final double c;
	@Getter
	private final double d;

	/**
	 * Construct a new matrix with the following parameters.
	 * @param a The <code>a</code> parameter of the matrix
	 * @param b The <code>b</code> parameter of the matrix
	 * @param c The <code>c</code> parameter of the matrix
	 * @param d The <code>d</code> parameter of the matrix
	 */
	public Matrix2(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
}
