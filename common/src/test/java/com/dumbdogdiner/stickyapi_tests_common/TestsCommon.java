/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi_tests_common;

import com.dumbdogdiner.stickyapi.StickyAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class TestsCommon {
    // Enums are counted in code coverage for some reason - so this function just invlokes valueOf on all enums it can find in a enum class.
    // https://stackoverflow.com/questions/4512358/emma-coverage-on-enum-types/4548912
    public static void superficialEnumCodeCoverage(Class<? extends Enum<?>> enumClass) {
        try {
            for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
                enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static Handler maskedHandler = new StreamHandler(System.out, new SimpleFormatter());
    private static List<Handler> handlers = new ArrayList<>();

    public static void disableHandlers() {
        Logger l = StickyAPI.getLogger();
        l.setUseParentHandlers(false);
        for (Handler h : l.getHandlers()) {
            handlers.add(h);
            l.removeHandler(h);
        }
    }

    public static void enableHandlers() {
        Logger l = StickyAPI.getLogger();
        l.setUseParentHandlers(true);
        for(Handler h : handlers){
            l.addHandler(h);
        }


    }

    public static void addMaskedHandler(){
        Logger l = StickyAPI.getLogger();
        l.addHandler(maskedHandler);
    }

    public static void removeMaskedHandler(){
        Logger l = StickyAPI.getLogger();
        l.removeHandler(maskedHandler);
    }
}
