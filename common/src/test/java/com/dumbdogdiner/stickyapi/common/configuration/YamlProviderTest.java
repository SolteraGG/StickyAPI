/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.dumbdogdiner.stickyapi.common.configuration.providers.YamlProvider;
import com.google.common.io.Files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class YamlProviderTest {

    private static final String exampleConfigPath = "src/test/resources/example.config.yml";
    private static final String exampleConfigPathTestOutput = "build/example.config.yml.test-save";

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
    public void testSaveFile() throws IOException {
        // Write the file
        exampleFileConfig.save("build/example.config.yml.test-save");
        // Assert that the original and saved files match (bear in mind that snakeyaml doesn't keep comments!)
        Files.equal(new File(exampleConfigPath), new File(exampleConfigPathTestOutput));
    }
}
