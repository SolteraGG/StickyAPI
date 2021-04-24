/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.vector;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base abstract class for an immutable vector.
 * @param <T>
 */
abstract class Vector<T> implements Cloneable {
	/**
	 * @return The number of dimensions this vector has.
	 */
	protected abstract int getDimensions();

	/**
	 * Get an array of values representing this vector.
	 * @return An array of values.
	 */
	protected abstract double[] getValues();

	/**
	 * Get the value of the target dimension.
	 * @param index The index of the dimension
	 * @return The value of the target dimension.
	 */
	protected double getDimension(int index) {
		Preconditions.checkArgument(index < this.getDimensions(), "Index out of range for vector dimension.");
		return this.getValues()[index];
	}

	@Override
	public boolean equals(Object vector) {
		// check if vector is null or is not a vector
		if (!(vector instanceof Vector)) {
			return false;
		}
		// iterate over each dimension and append square to accumulator
		for (int i = 0; i < this.getDimensions(); i++) {
			if (this.getDimension(i) != ((Vector<?>)vector).getDimension(i)) {
				return false;
			}
		}
		// all dimensions equal - must be same vector.
		return true;
	}

	@Override
	public int hashCode() {
		// use two prime numbers because they're hot and sexy and help with uniqueness
		int hash = 7;
		// iterate over each dimension and append hash to accumulator
		for (int i = 0; i < this.getDimensions(); i++) {
			hash = 31 * hash + Double.hashCode(this.getDimension(i));
		}
		return hash;
	}

	/**
	 * Add the target vector to this vector.
	 * @param vector The target vector
	 * @return A new {@link Vector}.
	 */
	abstract @NotNull Vector<T> add(@NotNull Vector<T> vector);

	/**
	 * Subtract the target vector from this vector.
	 * @param vector The target vector
	 * @return A new {@link Vector}.
	 */
	abstract @NotNull Vector<T> subtract(@NotNull Vector<T> vector);

	/**
	 * Return the vector from this vector to the target vector.
	 * @param vector The target vector
	 * @return The vector from this vector to the target vector.
	 */
	@NotNull Vector<T> to(@NotNull Vector<T> vector) {
		return vector.subtract(vector);
	};

	/**
	 * Scale this vector by the given scalar.
	 * @param scalar The scalar to scale by
	 * @return The scaled vector.
	 */
	abstract @NotNull Vector<T> scale(@NotNull Number scalar);

	/**
 	 * @return The magnitude of this vector.
	 */
	double abs() {
		double acc = 0;
		// iterate over each dimension and append square to accumulator
		for (int i = 0; i < this.getDimensions(); i++) {
			acc += Math.pow(this.getDimension(i), 2);
		}
		// return square root of accumulator
		return Math.sqrt(acc);
	}

	/**
	 * @return A unit vector of this vector.
	 */
	@NotNull Vector<T> normalize() {
		return this.scale(this.abs());
	}

	/**
	 * Compute the dot product of two vectors.
	 * @param vec The other vectors
	 * @return The dot product of the two vectors.
	 */
	double dot(@NotNull Vector<T> vec) {
		// ensure dimensions are equal.
		Preconditions.checkNotNull(vec);
		Preconditions.checkArgument(this.getDimensions() == vec.getDimensions(), "Cannot compute dot product of vectors with mismatching dimensions.");
		double acc = 0;
		// iterate over each dimension and append it to accumulator
		for (int i = 0; i < this.getDimensions(); i++) {
			acc += this.getDimension(i) * vec.getDimension(i);
		}
		// return the accumulator value.
		return acc;
	}
}
