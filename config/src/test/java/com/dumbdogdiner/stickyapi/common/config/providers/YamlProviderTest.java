/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.config.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dumbdogdiner.stickyapi.common.config.FileConfiguration;
import com.google.common.io.Files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class YamlProviderTest {

    private static final String exampleConfigPath = "src/test/resources/example.config.yml";

    private FileConfiguration exampleFileConfig;

    @BeforeAll
    public void prepareGetExampleFile() {
        try {
            exampleFileConfig = new YamlProvider(new FileInputStream(exampleConfigPath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFileAsFile() throws FileNotFoundException {
        new YamlProvider(new File(exampleConfigPath));
    }

    @Test
    public void testLoadFileAsInputStream() throws FileNotFoundException {
        new YamlProvider(new FileInputStream(exampleConfigPath));
    }

    @Test
    public void testGetValidString() {
        assertEquals("Hello, World!", exampleFileConfig.getString("string"));
    }

    @Test
    public void testGetValidStringWithDefault() {
        assertEquals("Hello, World!", exampleFileConfig.getString("string", "Goodbye, World!"));
    }

    @Test
    public void testGetNonExistentString() {
        assertNull(exampleFileConfig.getString("non-existent"));
    }

    @Test
    public void testGetNonExistentStringWithDefault() {
        assertEquals("Default Value", exampleFileConfig.getString("non-existent", "Default Value"));
    }

    @Test
    public void testSaveFileInputString() throws IOException {
        String testOutput = "build/example.config.yml.string.test-save";
        // Write the file
        assertTrue(exampleFileConfig.save(testOutput));
        // Assert that the original and saved files match (note that snakeyaml doesn't keep comments!)
        // This *could* throw an IOException
        Files.equal(new File(exampleConfigPath), new File(testOutput));
    }

    @Test
    public void testSaveFileInputFile() throws IOException {
        String testOutput = "build/example.config.yml.file.test-save";
        // Write the file
        assertTrue(exampleFileConfig.save(new File(testOutput)));
        // Assert that the original and saved files match (note that snakeyaml doesn't keep comments!)
        // This *could* throw an IOException
        Files.equal(new File(exampleConfigPath), new File(testOutput));
    }

    @Test
    public void testSaveFileInputStringInvalid() {
        assertFalse(exampleFileConfig.save("build"));
    }

    @Test
    public void testSaveFileInputFileInvalid() {
        assertFalse(exampleFileConfig.save(new File("build")));
    }

    @Test
    public void testAsynchronousRead() {
        // create a new threadpool with n threads.
        int threadCount = 3;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        AtomicBoolean didFail = new AtomicBoolean();
        // create n tasks and submit them to the pool.
        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                if (!exampleFileConfig.getString("string").equals("Hello, World!")) {
                    didFail.set(true);
                }
            });
        }
        // wait for pool to finish execution.
        pool.shutdown();
        assertFalse(didFail.get());
    }

    // This does not work yet - need to implement write.
    // @Test
    // public void testAsynchronousWrite() { }
}
