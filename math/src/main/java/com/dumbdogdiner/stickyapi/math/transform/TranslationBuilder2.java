package com.dumbdogdiner.stickyapi.math.transform;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies transformations sequentially.
 */
public class TranslationBuilder2 {
	@Getter
	private final List<Matrix2> transformations = new ArrayList<>();

	/**
	 * Apply the transformation to this builder.
	 * @param transformation The transformation to apply
	 * @return The {@link TranslationBuilder2} with the new matrix applied.
	 */
	public TranslationBuilder2 apply(Matrix2 transformation) {
		this.transformations.add(transformation);
		return this;
	}

	/**
	 * @return The translation builder as a {@link Matrix2}
	 */
	public Matrix2 toMatrix() {
		// TODO: implement
		return Matrix2.getIdentityMatrix();
	}
}
