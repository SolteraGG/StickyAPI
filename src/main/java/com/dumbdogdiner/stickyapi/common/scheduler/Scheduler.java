/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dumbdogdiner.stickyapi.common.scheduler;

import java.time.DateTimeException;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Queue;
import java.util.ArrayDeque;

import lombok.Getter;
import lombok.Setter;

/**
 * Schedule async or sync runnables
 */
// TODO: Make this... better...
public class Scheduler {
	/**
	 * Array of tasks to be run on the main thread or synchronously
	 */
	@Getter
	protected Queue<RunnableFuture<?>> synchronous = new ArrayDeque<RunnableFuture<?>>();
	/**
	 * Array of tasks to be run as part of a thread pool.
	 */
	@Getter
	protected Queue<RunnableFuture<?>> asynchronous = new ArrayDeque<RunnableFuture<?>>();

	/**
	 * The thread pool to use for executing tasks.
	 */
	@Setter
	protected ScheduledThreadPoolExecutor pool;

	public Scheduler(int poolsz) {
		this.pool = new ScheduledThreadPoolExecutor(poolsz);
	}

	/**
	 * Execute a function/task immediately asynchronously
	 */
	public <V> Future<V> scheduleThreaded(Callable<V> task) {
		RunnableFuture<V> t = new FutureTask<V>(task);
		this.pool.execute(t);
		return (FutureTask<V>) t;
	}

	public <T> Future<T> scheduleThreaded(Callable<T> task, Date exectime) {
		long future = exectime.getTime();
		long now = System.currentTimeMillis();
		if (future <= now)
			throw new DateTimeException("Get the time machine, morty! We're going back to the future!");

		long delay = future - now;
		return this.pool.schedule(task, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * Execute a task in the synchronous thread, scheduled for the next available
	 * tick.
	 */
	public <T> Future<T> scheduleSynchronous(Callable<T> task) {
		FutureTask<T> t = new FutureTask<T>(task);
		this.synchronous.add(t);
		return t;
	}

	public <T> Future<T> scheduleSynchronous(Callable<T> task, Date exectime) {
		// TODO: Make synchronous version of this?
		return new FutureTask<T>(task);
	}

	/**
	 * Run all pending synchronous calls until they're finished. NOTE: This should
	 * be called in the application's eventloop or in a single thread.
	 */
	public void schedule() {
		RunnableFuture<?> task = null;
		while ((task = this.synchronous.poll()) != null)
			task.run();
	}

}
