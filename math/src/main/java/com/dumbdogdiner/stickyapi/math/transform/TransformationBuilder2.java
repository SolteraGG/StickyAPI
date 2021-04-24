/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.transform;

import com.dumbdogdiner.stickyapi.math.vector.Vector2;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies transformations sequentially.
 */
public class TransformationBuilder2 {
	@Getter
	private final List<Matrix2> transformations = new ArrayList<>();

	/**
	 * Apply the transformation to this builder.
	 * @param transformation The transformation to apply
	 * @return The {@link TransformationBuilder2} with the new matrix applied.
	 */
	public TransformationBuilder2 apply(Matrix2 transformation) {
		this.transformations.add(transformation);
		return this;
	}

	/**
	 * @return The translation builder as a {@link Matrix2}
	 */
	public Matrix2 toMatrix() {
		Matrix2 out = Matrix2.getIdentityMatrix();
		for (Matrix2 matrix : this.transformations) {
			out = out.postMultiply(matrix);
		}
		return out;
	}

	/**
	 * Transform the target matrix through the translation represented by this builder.
	 * @param input The input vector
	 * @return The output {@link Vector2}
	 */
	public Vector2 transform(Vector2 input) {
		Matrix2 transform = this.toMatrix();
		return new Vector2(
			transform.getA() * input.getX() + transform.getB() * input.getY(),
			transform.getC() * input.getX() + transform.getD() * input.getY()
		);
	}
}
