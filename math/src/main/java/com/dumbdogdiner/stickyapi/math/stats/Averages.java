/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.math.stats;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for computing various types of average.
 */
public final class Averages {
	private Averages() {}

	/**
	 * Compute the mean of the target dataset.
	 * @param dataset The target dataset
	 * @return The mean of the target dataset
	 */
	public static double getMean(@NotNull List<@NotNull Number> dataset) {
		Preconditions.checkNotNull(dataset);
		Preconditions.checkArgument(!dataset.isEmpty(), "Cannot compute mean of an empty dataset");
		double acc = 0;
		// iterate over dataset and add values to accumulator
		for (Number val : dataset) {
			Preconditions.checkNotNull(val, "Cannot compute mean of a dataset containing a null value");
			acc += val.doubleValue();
		}
		// return acc / length
		return acc / dataset.size();
	}

	/**
	 * Compute the mean of the target dataset.
	 * @param dataset The target dataset
	 * @return The mean of the target dataset
	 */
	public static double getMean(int[] dataset) {
		return getMean(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the mean of the target dataset.
	 * @param dataset The target dataset
	 * @return The mean of the target dataset
	 */
	public static double getMean(long[] dataset) {
		return getMean(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the mean of the target dataset.
	 * @param dataset The target dataset
	 * @return The mean of the target dataset
	 */
	public static double getMean(double[] dataset) {
		return getMean(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the median of the target dataset. Not guaranteed to produce a value in the dataset.
	 * @param dataset The target dataset
	 * @return The median of the target dataset.
	 */
	public static double getMedian(@NotNull List<@NotNull Number> dataset) {
		Preconditions.checkNotNull(dataset);
		Preconditions.checkArgument(!dataset.isEmpty(), "Cannot compute median of an empty dataset");

		// sort the dataset
		dataset = dataset.stream().sorted().collect(Collectors.toList());
		// compute the center index of the dataset
		// integer division rounds down so this works
		int center = dataset.size() / 2;
		// median exists within the dataset
		if (dataset.size() % 2 == 1) {
			Number value = dataset.get(center);
			// ensure value is not null
			Preconditions.checkNotNull(value, "Cannot compute median of a dataset containing a null value");
			return value.doubleValue();
		}
		// median is between two values - TODO: comment about the minus sign below vvvv
		Number upper = dataset.get(center);
		Number lower = dataset.get(center - 1);
		Preconditions.checkNotNull(upper,"Cannot compute median of a dataset containing a null value");
		Preconditions.checkNotNull(lower,"Cannot compute median of a dataset containing a null value");
		return (upper.doubleValue() + lower.doubleValue()) / 2;
	}

	/**
	 * Compute the median of the target dataset. Not guaranteed to produce a value in the dataset.
	 * @param dataset The target dataset
	 * @return The median of the target dataset.
	 */
	public static double getMedian(int[] dataset) {
		return getMedian(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the median of the target dataset. Not guaranteed to produce a value in the dataset.
	 * @param dataset The target dataset
	 * @return The median of the target dataset.
	 */
	public static double getMedian(long[] dataset) {
		return getMedian(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the median of the target dataset. Not guaranteed to produce a value in the dataset.
	 * @param dataset The target dataset
	 * @return The median of the target dataset.
	 */
	public static double getMedian(double[] dataset) {
		return getMedian(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the mode (most common value) of the dataset. This method
	 * does not account for multiple modes.
	 * @param dataset The target dataset
	 * @return The mode of the dataset.
	 */
	public static double getMode(@NotNull List<@NotNull Number> dataset) {
		Preconditions.checkNotNull(dataset);
		Preconditions.checkArgument(!dataset.isEmpty(), "Cannot compute mode of an empty dataset");
		double mode = -1;
		int maxCount = 0;
		// iterate over the dataset
		for (Number target : dataset) {
			int count = 0;
			// iterate over the dataset again and compare
			for (Number number : dataset) {
				// only need to check for null here, since this will catch the outer loop anyway.
				Preconditions.checkNotNull(number, "Cannot compute mode of a dataset containing a null value");
				if (number.equals(target)) count++;
			}
			// if count of this value is greater than the max, set it as new max
			if (count > maxCount) {
				maxCount = count;
				mode = target.doubleValue();
			}
		}
		// return the mode
		return mode;
	}

	/**
	 * Compute the mode (most common value) of the dataset. This method
	 * does not account for multiple modes.
	 * @param dataset The target dataset
	 * @return The mode of the dataset.
	 */
	public static double getMode(int[] dataset) {
		return getMode(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the mode (most common value) of the dataset. This method
	 * does not account for multiple modes.
	 * @param dataset The target dataset
	 * @return The mode of the dataset.
	 */
	public static double getMode(long[] dataset) {
		return getMode(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}

	/**
	 * Compute the mode (most common value) of the dataset. This method
	 * does not account for multiple modes.
	 * @param dataset The target dataset
	 * @return The mode of the dataset.
	 */
	public static double getMode(double[] dataset) {
		return getMode(Arrays.stream(dataset).boxed().collect(Collectors.toList()));
	}
}
