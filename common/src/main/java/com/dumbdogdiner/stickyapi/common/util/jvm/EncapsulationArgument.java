/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.jvm;

import lombok.EqualsAndHashCode;
import lombok.Getter;

// --add-exports <source-module>/<package>=<target-module>(,<target-module>)*
@EqualsAndHashCode
public final class EncapsulationArgument {

    public enum EncapsulationType {
        ADD_OPENS, ADD_EXPORTS
    }

    @Getter
    private final EncapsulationType type;

    @Getter
    private final String sourceModule;

    @Getter
    private final String sourcePackage;

    @Getter
    private final String targetModule;

    public EncapsulationArgument(String argument, EncapsulationType type) {
        // Split out the input argument
        // (becomes something like ["--add-opens", "java.base/java.lang.reflect", "ALL-UNNAMED")
        String[] inputSplit = argument.split("=");
        
        String[] source = inputSplit[1].split("/"); // ["java.base", "java.lang.reflect"] for example

        this.type = type;

        this.sourceModule = source[0];
        this.sourcePackage = source[1];
        this.targetModule = inputSplit[2];
    }
}