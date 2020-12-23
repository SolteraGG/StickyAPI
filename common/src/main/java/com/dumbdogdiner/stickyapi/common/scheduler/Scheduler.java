/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.scheduler;

import java.time.DateTimeException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
     *
     * @param task to execute
     */
    public <V> Future<V> scheduleThreaded(Callable<V> task) {
        RunnableFuture<V> t = new FutureTask<V>(task);
        this.pool.execute(t);
        return (FutureTask<V>) t;
    }

    /**
     * Schedule a function/task to run asynchronously at a certain datetime
     *
     * @param task to run
     * @param time to execute the task
     */
    public <T> Future<T> scheduleThreaded(Callable<T> task, Date time) {
        long future = time.getTime();
        long now = System.currentTimeMillis();
        if (future <= now) throw new DateTimeException(
            "Get the time machine, morty! We're going back to the future!"
        );

        long delay = future - now;
        return this.pool.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Execute a task in the synchronous thread, scheduled for the next available
     * tick.
     *
     * @param task to run
     */
    public <T> Future<T> scheduleSynchronous(Callable<T> task) {
        FutureTask<T> t = new FutureTask<T>(task);
        this.synchronous.add(t);
        return t;
    }

    /**
     * Schedule a function/task to run synchronously at a certain datetime
     *
     * @param task to run
     * @param time to execute the task
     */
    public <T> Future<T> scheduleSynchronous(Callable<T> task, Date time) {
        // TODO: Make synchronous version of this?
        return new FutureTask<T>(task);
    }

    /**
     * Run all pending synchronous calls until they're finished. NOTE: This should
     * be called in the application's eventloop or in a single thread.
     */
    public void schedule() {
        RunnableFuture<?> task = null;
        while ((task = this.synchronous.poll()) != null) task.run();
    }
}
