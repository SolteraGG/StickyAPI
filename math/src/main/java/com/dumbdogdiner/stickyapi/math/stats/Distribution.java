/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.stats;

import com.google.common.base.Preconditions;

/**
 * Utility class for dealing with distribution of data.
 */
public final class Distribution {
	private Distribution() {}

	/**
	 * Calculate the expected value of the target dataset. This is just an alias for the mean
	 * of the dataset.
	 * @param dataset The target dataset
	 * @return The expected value of the target dataset.
	 */
	public static double expectation(double[] dataset) {
		return Averages.getMean(dataset);
	}

	/**
	 * Compute the variance of the target dataset. This is a measure of
	 * how much the data varies about its mean value.
	 * @param dataset The target dataset
	 * @return The variation of the target dataset.
	 */
	public static double variance(double[] dataset) {
		// compute squares of dataset
		double[] squares = new double[dataset.length];
		for (int i = 0; i < dataset.length; i++) {
			squares[i] = Math.pow(dataset[i], 2);
		}
		// Var(X) = E(X^2) - E(X)^2
		return expectation(squares) - Math.pow(expectation(dataset), 2);
	}

	/**
	 * Compute the standard deviation of the target dataset. This is a measure
	 * of how much the data varies about its mean value.
	 * @param dataset The target dataset
	 * @return The standard deviation of the target dataset.
	 */
	public static double standardDeviation(double[] dataset) {
		return Math.sqrt(variance(dataset));
	}

	/**
	 * Compute the covariance of two datasets.
	 * @param x The first dataset
	 * @param y The second dataset
	 * @return The covariance of the two datasets.
	 */
	public static double covariance(double[] x, double[] y) {
		Preconditions.checkArgument(x.length == y.length, "Datasets must be of equal size to compute covariance.");
		double[] products = new double[x.length];
		// compute product of values
		for (int i = 0; i < x.length; i++) {
			products[i] = x[i] * y[i];
		}
		// cov(X, Y) = E(XY) - E(X)E(Y)
		return expectation(products) - expectation(x) * expectation(y);
	}

	/**
	 * Compute the Pearson correlation coefficient (PCC) of the target datasets.
	 * This is a measure of how much two datasets correlate with each other.
	 * @param x The first dataset
	 * @param y The second dataset
	 * @return The PCC of the target datasets.
	 */
	public static double pcc(double[] x, double[] y) {
		Preconditions.checkArgument(x.length == y.length, "Datasets must be of equal size to compute PCC.");
		return covariance(x, y) / (standardDeviation(x) * standardDeviation(y));
	}
}
