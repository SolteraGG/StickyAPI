/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.vector;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a three-dimensional immutable vector.
 */
public class Vector3 extends Vector<Vector3> {
	/**
	 * @return A zero vector.
	 */
	public static Vector3 zero() {
		return new Vector3(0, 0, 0);
	}

	/**
	 * Create a new Vector3 from polar co-ordinates.
	 * @param r The radius of the vector
	 * @param theta The 1st angle from the initial line
	 * @param phi The 2nd angle from the initial line
	 * @return A {@link Vector3}
	 */
	public static Vector3 fromPolar(double r, double theta, double phi) {
		return new Vector3(
			r * Math.sin(theta) * Math.cos(phi),
			r * Math.sin(theta) * Math.sin(phi),
			r * Math.cos(theta)
		);
	}

	/**
	 * The x value of this vector.
	 */
	@Getter
	private final double x;

	/**
	 * The y value of this vector.
	 */
	@Getter
	private final double y;

	/**
	 * The z value of this vector.
	 */
	@Getter
	private final double z;

	/**
	 * Construct a new 3D vector.
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Construct a new 3D vector.
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 */
	public Vector3(@NotNull Number x, @NotNull Number y, @NotNull Number z) {
		// ensure arguments are not null
		Preconditions.checkNotNull(x);
		Preconditions.checkNotNull(y);
		Preconditions.checkNotNull(z);
		this.x = x.doubleValue();
		this.y = y.doubleValue();
		this.z = z.doubleValue();
	}

	/**
	 * Construct a new 3D vector from an existing vector.
	 * @param target The existing vector
	 */
	public Vector3(@NotNull Vector3 target) {
		Preconditions.checkNotNull(target);
		this.x = target.x;
		this.y = target.y;
		this.z = target.z;
	}

	@Override
	protected int getDimensions() {
		return 3;
	}

	@Override
	protected double[] getValues() {
		return new double[]{this.x, this.y, this.z};
	}

	@Override
	@NotNull
	public Vector3 add(@NotNull Vector<Vector3> vector) {
		return new Vector3(this.x + vector.getDimension(0), this.y + vector.getDimension(1), this.z + vector.getDimension(2));
	}

	@Override
	@NotNull
	Vector3 subtract(@NotNull Vector<Vector3> vector) {
		return new Vector3(this.x - vector.getDimension(0), this.y - vector.getDimension(1), this.z - vector.getDimension(2));
	}

	@Override
	@NotNull
	Vector3 scale(@NotNull Number scalar) {
		return new Vector3(this.x * scalar.doubleValue(), this.y * scalar.doubleValue(), this.z * scalar.doubleValue());
	}
}
