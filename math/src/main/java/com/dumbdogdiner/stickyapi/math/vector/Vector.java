/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.vector;

import com.sun.net.httpserver.HttpServer;

/**
 * Base abstract class for an immutable vector.
 * @param <T>
 */
abstract class Vector<T> {
	/**
	 * @return The number of dimensions this vector has.
	 */
	abstract int getDimensions();

	abstract Vector<T> add(Vector<T> vector);

	abstract Vector<T> subtract(Vector<T> vector);

	/**
	 * Return the vector from this vector to the target vector.
	 * @param vector The target vector
	 * @return The vector from this vector to the target vector.
	 */
	Vector<T> to(Vector<T> vector) {
		return vector.subtract(vector);
	};

	/**
	 * Scale this vector by the given scalar.
	 * @param scalar The scalar to scale by
	 * @return The scaled vector.
	 */
	abstract Vector<T> scale(Number scalar);

	/**
 	 * @return The magnitude of this vector.
	 */
	abstract double abs();

	/**
	 * @return A unit vector of this vector.
	 */
	Vector<T> normalize() {
		return this.scale(this.abs());
	}

	/**
	 * Compute the dot product of two vectors.
	 * @param vec The other vectors
	 * @return The dot product of the two vectors.
	 */
	abstract double dot(T vec);
}
