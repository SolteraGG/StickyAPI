/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.configuration.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.dumbdogdiner.stickyapi.common.configuration.FileConfiguration;

import org.apache.commons.lang.Validate;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlProvider implements FileConfiguration {

    private Map<String, Object> data;

    public YamlProvider(InputStream stream) {
        Validate.notNull(stream, "InputStream cannot be null!");

        // Attempt to load the stream

        Yaml yaml = new Yaml();

        Map<String, Object> obj = yaml.load(stream);

        this.data = obj;
        
    }

    public YamlProvider(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

	@Override
	public String getString(String path, String def) {
        Object value = data.get(path);
        return (value != null) ? value.toString() : def;
	}

	@Override
	public String getString(String path) {
        Object value = data.get(path);
        return (value != null) ? value.toString() : null;
	}

    @Override
    public void save(String path) throws IOException {
        // Generate dumper options
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);

        // Create a new yaml object with our options
        Yaml yaml = new Yaml(options);

        // Write the data
        FileWriter fileWriter = new FileWriter(path);
        yaml.dump(data, fileWriter);

    }
}
