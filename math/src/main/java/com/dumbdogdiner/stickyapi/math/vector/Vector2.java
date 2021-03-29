/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.vector;

import lombok.Getter;

/**
 * Represents a two dimensional vector.
 */
public class Vector2 extends Vector<Vector2> {
	/**
	 * The x value of this vector.
	 */
	@Getter
	private double x;

	/**
	 * The y value of this vector.
	 */
	@Getter
	private double y;

	/**
	 * Construct a new 2D vector.
	 * @param x The x value
	 * @param y The y value
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	int getDimensions() {
		return 2;
	}

	@Override
	Vector<Vector2> add(Vector<Vector2> vector) {
		return new Vector2(this.x + vector.x, this.y + vector.y);
	}

	@Override
	Vector<Vector2> subtract(Vector<Vector2> vector) {
		return null;
	}

	@Override
	Vector2 scale(Number scalar) {
		return new Vector2(this.x * scalar.doubleValue(), this.y * scalar.doubleValue());
	}

	@Override
	double abs() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}

	@Override
	double dot(Vector2 vec) {
		return this.x * vec.x + this.y * vec.y;
	}
}
