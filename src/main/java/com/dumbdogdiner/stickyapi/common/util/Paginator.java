/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.lang.Math;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Paginator<T> {
	private T[] objects;
	private Double pagSize;
	private Integer currentPage;
	private Integer amountOfPages;

	/**
	 * Create a pagination of the object array
	 * 
	 * @param objects array of objects to paginate
	 * @param max     maximum number of objects per page
	 */
	public Paginator(@NotNull T[] objects, @NotNull Integer max) {
		this.objects = objects;
		this.pagSize = Double.valueOf(max);
		this.amountOfPages = (int) Math.ceil(objects.length / pagSize);
	}

	/**
	 * Create a pagination of the list of objects
	 * 
	 * @param objects an object list
	 * @param max     maximum number of objects per page
	 */
	public Paginator(@NotNull List<T> objects, @NotNull Integer max) {
		this(objects.toArray((T[]) new Object[0]), max);
	}

	/**
	 * Set the paginated objects to the list below
	 * 
	 * @param objects objects to replace the existing object list for pagination
	 */
	public void setElements(@NotNull List<T> objects) {
		this.objects = objects.toArray((T[]) new Object[0]);
		this.amountOfPages = (int) Math.ceil(objects.size() / pagSize);
	}

	/**
	 * Test if the paginator has another page.
	 * 
	 * @return True if there's another page
	 */
	public boolean hasNext() {
		return currentPage < amountOfPages;
	}

	/**
	 * Test if the paginator has a previous page
	 * 
	 * @return True if there is a previous page
	 */
	public boolean hasPrev() {
		return currentPage > 1;
	}

	/**
	 * Get the next page number
	 * 
	 * @return Next page number
	 */
	public int getNext() {
		return currentPage + 1;
	}

	/**
	 * Get the previous page number
	 * 
	 * @return Previous page number
	 */
	public int getPrev() {
		return currentPage - 1;
	}

	/**
	 * Get the current page number
	 * 
	 * @return current page number
	 */
	public int getCurrent() {
		return currentPage;
	}

	/**
	 * Get the total number of pages for the objects in the array
	 * 
	 * @return total number of pages
	 */
	public int getTotalPages() {
		return this.amountOfPages;
	}

	/**
	 * Get the objects for this page
	 * 
	 * @param pageNum the page number.
	 * @return List of objects that make up this page
	 */
	public List<T> getPage(@NotNull Integer pageNum) {
		List<T> page = new ArrayList<>();
		double total = objects.length / pagSize;
		amountOfPages = (int) Math.ceil(total);
		currentPage = pageNum;

		if (objects.length == 0)
			return page;

		double startC = pagSize * (pageNum - 1);
		double finalC = startC + pagSize;

		for (; startC < finalC; startC++) {
			if (startC < objects.length)
				page.add(objects[(int) startC]);
		}

		return page;
	}
}
