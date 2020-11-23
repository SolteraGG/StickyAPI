/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.dumbdogdiner.stickyapi.common.util.Debugger;

import lombok.Getter;
import lombok.Setter;

/**
 * General purpose cache for caching things that should be cached.
 */
public class Cache<T extends Cacheable> {
    public interface CacheTester<T extends Cacheable> {
        /**
         * Matching function that should evaluate to true if a given object matches 
         * any necessary criteria.
         * @param object The object being tested
         * @return True if this object matches the necessary criteria.
         */
        boolean match(T object);
    }

    private ConcurrentHashMap<String, T> objects = new ConcurrentHashMap<String, T>();
    private ConcurrentHashMap<String, Long> objectInsertionTimestamps = new ConcurrentHashMap<String, Long>();

    @Getter @Setter private Long ttl = (long) (30 * 60e3);

    // private int memoryUsage = 0;
    // @Getter @Setter
    // private int maxMemoryUsage = 0;

    @Getter @Setter
    private int maxSize = 0;
    
    @Getter private FutureTask<?> objectExpiryTask =  new FutureTask<>(new Callable<Boolean>() {
            @Override
        public Boolean call() {
                if (ttl <= 0) {
                    return false;
                }
                
                objectInsertionTimestamps.forEach((k, v) -> {
                    if (v + ttl < System.currentTimeMillis()) {
                        debug.print("Evicting " + k + " from " + clazz.getSimpleName() + " cache");
                        removeKey(k);
                    }
                });
                return true;
            }
        });


    private Class<T> clazz;
    public Cache(Class<T> clazz) {
        this.clazz = clazz;
    }

    private Debugger debug = new Debugger(getClass());

    /**
     * Return the size of this cache.
     * @return The size of this cache.
     */
    public int size() {
        return objects.size();
    }

    // /**
    //  * Get the memory usage of this cache, specifying the units of the returned value.
    //  * @param units The units the result
    //  * @return Approximate memory usage
    //  */
    // public Double memoryUsage(MemoryUtil.Unit units) {
    //     return MemoryUtil.formatBits(memoryUsage, units);
    // }   

    // /**
    //  * Get the approximate memory usage of this cache in kilobytes.
    //  * @return Approximate memory usage
    //  */
    // public Double memoryUsage() {
    //     return memoryUsage(MemoryUtil.Unit.KILOBYTES);
    // }

    /**
     * Retrieve an object from the cache.
     * @param key The key of the object
     * @return The requested object, if it exists
     */
    public T get(String key) {
        debug.reset();
        T object = objects.get(key);
        
        if (object != null)
            debug.print("Got cached entry for " + clazz.getSimpleName() + " with key " + key);

        return object;
    }

    /**
     * Return all values in the cache.
     * @return All values in the cache
     */
    public Collection<T> getAll() {
        return objects.values();
    }

    /**
     * Find an object using the given tester lambda.
     * @param tester A cache tester implemented for any necessary criteria you are looking for
     * @return The first object that evaluates the tester to true, if there is one
     */
    public T find(CacheTester<T> tester) {
        debug.reset();
        for (T object : objects.values()) {
            if (tester.match(object)) {
                debug.print("Found cached entry for " + clazz.getSimpleName() + " with key " + object.getKey());
                return object;
            }
        }
        debug.print("Failed to find " + clazz.getSimpleName() + " using parsed matcher");
        return null;
    }

    /**
     * Store an object in the cache.
     * @param object The object to store
     */
    public void put(T object) {
        debug.reset();

        if (objects.containsKey(object.getKey())) {
            debug.print("Skipping insertion for " + clazz.getSimpleName() + " " + object.getKey() + " - already exists.");
            return;
        }

        if (maxSize > 0) {
            while (objects.size() >= maxSize) {
                removeOldestEntry();
            }
        }

        // This causes a StackOverflow, no big deal just remove this feature!
        // Or, you know, fix memory util but that's more work than commenting a few lines
        // which didn't really work in the first place
        // int size = MemoryUtil.getSizeOf(object);
        // if (maxMemoryUsage > 0 && memoryUsage + size > maxMemoryUsage) {
        //     Runnable memoryReleaser = new Runnable() {
        //         @Override
        //         public void run() {
        //             synchronized (this) {
        //                 while (memoryUsage + size > maxMemoryUsage) {
        //                     removeOldestEntry();
        //                 }
        //                 memoryUsage += size;
        //                 debug.print("Released memory from " + clazz.getSimpleName() +" cache");
        //             }
        //         }
        //     };
        //     StickyAPI.getPool().submit(memoryReleaser);
        // }

        objects.put(object.getKey(), object);
        objectInsertionTimestamps.put(object.getKey(), System.currentTimeMillis());
        debug.print("Created cached entry for " + clazz.getSimpleName() + " with key " + object.getKey());
    }

    /**
     * Update a value in the cache - bypasses not-null check!
     * @param object The object to update
     */
    public void update(T object) {
        if (objects.containsKey(object.getKey())) {
            remove(object);
        }
        put(object);
    }

    /**
     * Remove an object from the cache, returning the old object.
     * @param object The object to remove
     * @return The removed object, if it exists
     */
    public T remove(T object) {
        debug.reset();
        T didRemove = objects.remove(object.getKey());

        if (didRemove == null) {
            debug.print("Could not remove entry for " + clazz.getSimpleName() + " with key " + object.getKey() + " - does not exist");
            return null;
        }

        // if (maxMemoryUsage > 0) {
        //     memoryUsage -= MemoryUtil.getSizeOf(object);
        // }
        
        debug.print("Removed entry for " + clazz.getSimpleName() + " with key " + object.getKey());
        return didRemove;
    }

    /**
     * Remove an oibject from the cache using its key.
     * @param key The key to remove
     * @return The removed object, if it exists
     */
    public T removeKey(String key) {
        T object = objects.get(key);
        if (object == null) {
            return null;
        }

        return remove(object);
    }

    /**
     * Fetch the oldest entry in the cache.
     * @return The oldest entry in the cache, if it exists
     */
    public T getOldestEntry() {
        String oldest = null;
        Long oldestTimestamp = Long.MAX_VALUE;

        for (String k : objectInsertionTimestamps.keySet()) {
            Long v = objectInsertionTimestamps.get(k);
            if (v < oldestTimestamp) {
                oldest = k;
                oldestTimestamp = v;
            }
        }
        return objects.get(oldest);
    }

    /**
     * Remove the oldest entry from the cache.
     * @return The oldest entry in the cache, if it exists  
     */
    public T removeOldestEntry() {
        return remove(getOldestEntry());
    }
}
