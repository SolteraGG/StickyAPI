/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.translation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.dumbdogdiner.stickyapi.common.util.Debugger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class LocaleProviderTest {

    private static final File localeDirectoryGroup1 = new File("src/test/resources/localeprovider/group1");

    private static final File nonExistentDirectory = new File("build/localeprovider-newfolder");

    private LocaleProvider localeProviderGroup1;

    // Used for tests 2/3
    private ConcurrentHashMap<String, Locale> loadedLocales;

    @BeforeAll
    public void setup() {
        // Enable the StickyAPI debugger
        Debugger.setEnabled(true);
        
        // Create locale providers
        localeProviderGroup1 = new LocaleProvider(localeDirectoryGroup1);

        // Delete the non-existent testing directory if it exists
        if (nonExistentDirectory.exists()) {
            assertTrue(nonExistentDirectory.delete()); // Attempt to delete the directory
        }
    }

    @Test
    @Order(1)
    public void testLoadLocaleString() {
        assertTrue(localeProviderGroup1.loadLocale("messages.en_us.yml"));
    }
    
    @Test
    @Order(2)
    public void testLoadedLocalesSizeOneLoaded() {
        loadedLocales = localeProviderGroup1.getLoadedLocales();
        assertEquals(1, loadedLocales.size());
    }

    @Test
    @Order(3)
    public void testLoadedLocalesContentOneLoaded() {
        Locale onlyLocale = (Locale) loadedLocales.values().toArray()[0];
        assertEquals(localeProviderGroup1.getLocale("messages.en_us"), onlyLocale);
    }

    @Test
    @Order(4)
    public void testGetDefaultLocaleNull() {
        assertEquals(null, localeProviderGroup1.getDefaultLocale());
    }

    @Test
    @Order(5)
    public void testSetDefaultLocaleLoaded() {
        assertTrue(localeProviderGroup1.setDefaultLocale("messages.en_us"));
    }

    @Test
    @Order(6)
    public void testSetDefaultLocaleNotLoaded() {
        assertFalse(localeProviderGroup1.setDefaultLocale("not-loaded-locale"));
    }

    @Test
    @Order(7)
    public void testGetDefaultLocale() {
        assertEquals(localeProviderGroup1.getLocale("messages.en_us"), localeProviderGroup1.getDefaultLocale());
    }

    @Test
    @Order(8)
    public void testLoadLocaleSameOneTwiceWithExtension() {
        // Should return false and debug "already loaded"
        assertFalse(localeProviderGroup1.loadLocale("messages.en_us.yml"));
    }

    @Test
    @Order(9)
    public void testLoadLocaleSameOneTwiceNoExtension() {
        // Should return false and debug "already loaded"
        assertFalse(localeProviderGroup1.loadLocale("messages.en_us"));
    }

    @Test
    @Order(10)
    public void testLoadLocaleNonExistentFile() {
        // Should return false and debug "could not find file"
        assertFalse(localeProviderGroup1.loadLocale("non-existent-locale.yml"));
    }

    @Test
    @Order(11)
    public void testTranslate() {
        String output = localeProviderGroup1.translate("player-has-not-joined", new HashMap<String, String>() {{
            put("prefix", "Msg>");
            put("target", "Notch");
        }});

        // Translate changes & to § for ChatColor codes 
        assertEquals("Msg>§cError! The player Notch has not joined before!", output);
    }

    @Test
    @Order(12)
    public void testTranslateNonExistentNode() {
        // should return null and debug "node does not exist"
        assertNull(localeProviderGroup1.translate("non-existent", new HashMap<>()));
    }

    @Test
    @Order(13)
    public void testTranslateEmptyNode() {
        // should return null and debug "invalid node name"
        assertNull(localeProviderGroup1.translate("", new HashMap<>()));
    }

    @Test
    @Order(14)
    public void testTranslateNullNode() {
        // should return null and debug "invalid node name"
        assertNull(localeProviderGroup1.translate(null, new HashMap<>()));
    }

    @Test
    @Order(15)
    public void testTranslateNoColor() {
        String output = localeProviderGroup1.translateNoColor("player-has-not-joined", new HashMap<String, String>() {{
            put("prefix", "Msg>");
            put("target", "Notch");
        }});

        assertEquals("Msg>&cError! The player Notch has not joined before!", output);
    }

    @Test
    @Order(16)
    public void testTranslateNoColorNonExistentNode() {
        // should return null and debug "node does not exist"
        assertNull(localeProviderGroup1.translateNoColor("non-existent", new HashMap<>()));
    }

    @Test
    @Order(17)
    public void testTranslateNoColorEmptyNode() {
        // should return null and debug "invalid node name"
        assertNull(localeProviderGroup1.translateNoColor("", new HashMap<>()));
    }

    @Test
    @Order(18)
    public void testTranslateNoColorNullNode() {
        // should return null and debug "invalid node name"
        assertNull(localeProviderGroup1.translateNoColor(null, new HashMap<>()));
    }

    /*
    Cannot be tested right now...
    
    public void testLoadLocaleInvalidFile() {
        // Should return false and debug "encountered an error - skipping"
        assertFalse(localeProviderGroup1.loadLocale("invalid.yml"));
    }*/

    @Test
    public void testCreateLocaleProviderWithNonExistentFolder() {
        // Make sure the file does not exist
        assertFalse(nonExistentDirectory.exists());
        
        // Create a new instance of LocaleProvider (will create the directory)
        new LocaleProvider(nonExistentDirectory);
        
        // Make sure that it now exists and is a directory
        assertTrue(nonExistentDirectory.exists());
        assertTrue(nonExistentDirectory.isDirectory());
    }

}
