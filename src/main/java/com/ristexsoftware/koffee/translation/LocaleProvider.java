/* 
 *  Koffee - A simple collection of utilities I commonly use
 *  Copyright (C) 2019-2020 Justin Crawford <justin@Stacksmash.net>
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

package com.ristexsoftware.koffee.translation;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.ristexsoftware.koffee.util.Debugger;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides an interface between locale files and your plugin.
 */
public class LocaleProvider {
    Debugger debug = new Debugger(getClass());

    @Getter
    @Setter
    private File localeFolder;

    @Getter
    private ConcurrentHashMap<String, Locale> loadedLocales = new ConcurrentHashMap<>();

    /**
     * The default locale to use when
     */
    @Getter
    private Locale defaultLocale;

    /**
     * Construct a new LocaleProvider using the target folder for storing/loading locales.
     * @param localeFolder The target folder
     * @return A new LocaleProvider instance
     */
    public LocaleProvider(File localeFolder) {
        this.localeFolder = localeFolder;
        if (!localeFolder.exists())
            localeFolder.mkdir();
    }

    /**
     * Load a locale with the given name.
     * @param name The name of the locale to load
     * @return True if the load was successful
     */
    public boolean loadLocale(String name) {
        if (!name.endsWith(".yml"))
            name += ".yml";

        return loadLocale(new File(localeFolder, name));
    }

    /**
     * Load a locale using a file.
     * @param file The file containing the locale configuration
     * @return True if the load was successful
     */
    public boolean loadLocale(File file) {
        debug.reset().print("Looking for localization in " + file.getName() + "...");

        if (!file.exists() || file.isDirectory()) {
            debug.print("Could not find file - does not exist, or is directory");
            return false;
        }

        // Ensure the same locale isn't loaded twice.
        if (loadedLocales.containsKey(file.getName().substring(0, file.getName().length() - 4))) {
            debug.print("Skipping loading locale - already loaded");
            return false;
        }

        Locale locale = new Locale(file);
        if (!locale.getIsValid()) {
            debug.print("Encountered an error while loading the locale configuration - skipping load");
            return false;
        }

        loadedLocales.put(file.getName().substring(0, file.getName().length() - 4), locale);
        debug.print("Successfully loaded locale '" + file.getName().substring(0, file.getName().length() - 4) + "'");

        return true;
    }

    /**
     * Load all available locales.
     * @return The number of new locales loaded.
     */
    public int loadAllLocales() {
        int accumulator = 0;

        for (File file : this.localeFolder.listFiles()) {
            if (!file.getName().endsWith(".yml"))
                continue;

            if (loadLocale(file))
                ++accumulator;
        }

        debug.print("Loaded " + accumulator + " locales");
        return accumulator;
    }

    /**
     * Translate a localization with the given variables.
     * @param node The configuration node to retrieve
     * @param vars A map of variables to interpolate into the configured node value
     * @return The configured node string, with vars interpolated when required
     */
    public String translate(String node, Map<String, String> vars) {
        debug.reset();

        if (node == null || node.equals("")) {
            debug.print("invalid node name");
            return null;
        }

        String message = get(node);
        if (message == null) {
            debug.print("node does not exist");
            return null;
        }

        return Translation.translate(this, message, "&", vars);
    }

    /**
     * Translate a localization without color.
     * @param node The configuration node to retrieve
     * @param vars A map of variables to interpolate into the configured node value
     * @return The configured node string without color, with vars interpolated when required
     */
    public String translateNoColor(String node, Map<String, String> vars) {
        if (node == null || node.equals(""))
            return null;

        String message = get(node);
        if (message == null)
            return null;

        return Translation.translateVariables(this, message, vars);
    }

    /**
     * Get a localized value using the default locale.
     * @param node The configuration node to retrieve
     * @return A configured node string before interpolation of variables has been performed
     */
    public String get(String node) {
        return defaultLocale == null ? null : defaultLocale.get(node);
    }

    /**
     * Get a localized value using an enum of node values.
     * @param node The configuration node to retrieve
     * @return A configured node string before interpolation of variables has been performed
     */
    public String get(Enum<?> node) {
        return get(node.name().toLowerCase().replace("_", "-"));
    }

    /**
     * Get a localized value using the specified locale.
     * @param name The name of the locale to fetch from
     * @param node The configuration node to retrieve
     * @return A configured node string before interpolation of variables has been performed
     */
    public String get(String name, String node) throws IllegalArgumentException {
        checkForLoadedLocale(name);
        return loadedLocales.get(name).get(node);
    }

    /**
     * Get a variable, falling back to its default if it doesn't exist.
     * @param node The configuration node to retrieve
     * @return The variable, or the default for the given name if the former does not exist
     */
    public String getDefault(String node, String defaultValue) {
        return get(node) == null ? defaultValue : get(node);
    }

    /**
     * Fetch a loaded locale.
     * @param name The name of the locale 
     * @return The requested locale
     */
    public Locale getLocale(String name) throws IllegalArgumentException {
        checkForLoadedLocale(name);
        return loadedLocales.get(name);
    }

    /**
     * Set the default locale to use.
     * @param name The name of the locale
     * @return True if the default locale was set without error.
     */
    public boolean setDefaultLocale(String name) {
        try {
            checkForLoadedLocale(name);
        } catch (IllegalArgumentException e) {
            return false;
        }

        defaultLocale = loadedLocales.get(name);
        return true;
    }

    /**
     * Save an internal resource to the data file.
     * @param in File inputstream
     * @param resourcePath The path to which the resource should be saved
     * @param replace Whether or not to replace the file if it already exists
     */
    public void writeLocaleStream(InputStream in, String resourcePath, boolean replace)
            throws IllegalArgumentException, IOException {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("Resource path cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
        }

        File outFile = new File(localeFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(localeFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (outFile.exists() && !replace) {
            System.out.println("resource already exists");
            return;
        }

        OutputStream out = new FileOutputStream(outFile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }

    /**
     * Checks if a locale with the given name is loaded. Throws `IllegalArgumentException` if not found.
     */
    private void checkForLoadedLocale(String name) throws IllegalArgumentException {
        if (!loadedLocales.containsKey(name))
            throw new IllegalArgumentException("Locale " + name + " is not loaded");
    }

    /**
     * Convenience function for getting a new TreeMap 
     * @return {@link java.util.TreeMap}
     */
    public TreeMap<String, String> newVariables() {
        return new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    }
}