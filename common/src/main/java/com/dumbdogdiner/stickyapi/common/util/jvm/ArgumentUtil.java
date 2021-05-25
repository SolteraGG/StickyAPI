/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.jvm;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dumbdogdiner.stickyapi.common.util.jvm.EncapsulationArgument.EncapsulationType;

public final class ArgumentUtil {
    private ArgumentUtil() {
    }

    public static List<String> getJVMArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    /**
     * Gets a list of the --add-open flags used to start the JVM, in string format.
     * @return
     */
    private static List<String> getAddOpensStr() {
        return getJVMArgs().stream().filter(arg -> arg.startsWith("--add-opens=")).collect(Collectors.toList());
    }

    public static List<EncapsulationArgument> getAddOpens() {
        List<EncapsulationArgument> arguments = new ArrayList<EncapsulationArgument>();

        getAddOpensStr().forEach(argument -> {
            arguments.add(new EncapsulationArgument(argument, EncapsulationType.ADD_OPENS));
        });

        return arguments;
    }
}