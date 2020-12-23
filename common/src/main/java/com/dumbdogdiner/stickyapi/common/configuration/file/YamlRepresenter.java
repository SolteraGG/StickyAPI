/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.configuration.file;

import com.dumbdogdiner.stickyapi.common.configuration.ConfigurationSection;
import com.dumbdogdiner.stickyapi.common.configuration.serialization.ConfigurationSerializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

public class YamlRepresenter extends Representer {

    public YamlRepresenter() {
        this.multiRepresenters.put(
                ConfigurationSection.class,
                new RepresentConfigurationSection()
            );
        this.multiRepresenters.put(
                ConfigurationSerializable.class,
                new RepresentConfigurationSerializable()
            );
    }

    private class RepresentConfigurationSection extends RepresentMap {

        @NotNull
        @Override
        public Node representData(@NotNull Object data) {
            return super.representData(
                ((ConfigurationSection) data).getValues(false)
            );
        }
    }

    private class RepresentConfigurationSerializable extends RepresentMap {

        @NotNull
        @Override
        public Node representData(@NotNull Object data) {
            ConfigurationSerializable serializable = (ConfigurationSerializable) data;
            Map<String, Object> values = new LinkedHashMap<String, Object>();
            values.putAll(serializable.serialize());

            return super.representData(values);
        }
    }
}
