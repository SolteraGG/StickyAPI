/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.transform;

import lombok.Getter;

import java.util.Objects;

/**
 * Represents a 2x2 mathematical, immutable matrix. The matrix has the following layout:
 * <pre>
 * (a b)
 * (c d)
 * </pre>
 */
public class Matrix2 implements Cloneable {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Matrix2 matrix2 = (Matrix2) o;
		return Double.compare(matrix2.a, a) == 0 && Double.compare(matrix2.b, b) == 0 && Double.compare(matrix2.c, c) == 0 && Double.compare(matrix2.d, d) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b, c, d);
	}

	/**
	 * Add the target matrix to this matrix.
	 * @param target The target matrix
	 * @return The resulting {@link Matrix2}.
	 */
	public Matrix2 add(Matrix2 target) {
		return new Matrix2(
			this.a + target.a,
			this.b + target.b,
			this.c + target.c,
			this.b + target.b
		);
	}

	/**
	 * Subtract the target matrix from this matrix.
	 * @param target The target matrix
	 * @return The resulting {@link Matrix2}.
	 */
	public Matrix2 subtract(Matrix2 target) {
		return new Matrix2(
			this.a - target.a,
			this.b - target.b,
			this.c - target.c,
			this.d - target.d
		);
	}

	/**
	 * Scale this matrix by the target scalar.
	 * @param k The target scalar
	 * @return The resulting scaled {@link Matrix2}.
	 */
	public Matrix2 scale(double k) {
		return new Matrix2(
			this.a * k,
			this.b * k,
			this.c * k,
			this.d * k
		);
	}

	/**
	 * Pre-multiply the target matrix with this matrix.
	 * @param target The matrix being pre-multiplied.
	 * @return The resulting {@link Matrix2}
	 */
	public Matrix2 preMultiply(Matrix2 target) {
		return new Matrix2(
			target.a * this.a + target.b * this.c,
			target.a * this.b + target.b * this.d,
			target.c * this.a + target.d * this.c,
			target.c * this.b + target.d * this.d
		);
	}

	/**
	 * Post-multiply the target matrix with this matrix. This is equivalent
	 * to pre-multiplying the input matrix with this matrix.
	 * @param target The matrix being post-multiplied.
	 * @return The resulting {@link Matrix2}
	 */
	public Matrix2 postMultiply(Matrix2 target) {
		return target.preMultiply(this);
	}
}
