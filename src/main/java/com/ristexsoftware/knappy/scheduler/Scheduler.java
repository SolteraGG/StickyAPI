/* 
 *  Knappy - A simple collection of utilities I commonly use
 *  Copyright (C) 2019-2020 Zachery Coleman <Zachery@Stacksmash.net>
 *  Copyright (C) 2019-2020 Skye Elliot <actuallyori@gmail.com>
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

// package com.ristexsoftware.knappy.scheduler;

// import java.util.concurrent.Callable;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Future;
// import java.util.concurrent.FutureTask;
// import java.util.TimerTask;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.Timer;

// import com.ristexsoftware.knappy.util.Debugger;

// import lombok.Getter;
// import lombok.Setter;

// public class Scheduler {

//     /**
//      * Stores a hashmap of currently active tasks.
//      */
//     @Getter
//     private ConcurrentHashMap<Integer, Future<?>> tasks = new ConcurrentHashMap<>();

//     @Getter
//     private Integer nextTaskId = 0;

//     private Debugger debug = new Debugger(this.getClass());

//     /**
//      * The thread pool to use for executing tasks.
//      */
//     @Setter
//     private ExecutorService pool;

//     public Scheduler() { }
    
//     /**
//      * Run a task asynchronously.
//      */
//     public <T> Future<T> runTask(Callable<T> task) {
//         return pool.submit(task);
//     }

//     /**
//      * Schedule a task to be run later.
//      */
//     public int schedule(Callable<?> task, Long offset) {

//         Callable<?> taskToSubmit = new Callable<?>() {
//             public T call() throws Exception {
//                 Thread.sleep(offset);
//                 return task.call();
//             }
//         };

//         Future<?> futureTask = pool.submit(taskToSubmit);
//         tasks.set(++nextTaskId, futureTask);

//         return ++nextTaskId;
//     }

//     /**
//      * Run a timer task.
//      */
//     public void scheduleTimerTask(TimerTask task) {
//         runTask(new Callable<Void>() {
//             @Override
//             public Void call() {
//                 task.run();
//                 return null;
//             }
//         });
//         return;
//     }

// }
