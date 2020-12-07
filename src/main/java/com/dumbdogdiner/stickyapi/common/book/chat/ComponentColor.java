/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.chat;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ComponentColor {
    private static final Set<String> VALID_NAMED_COLORS = new HashSet<>();

    public static final ComponentColor RESET = registerValid("reset");
    public static final ComponentColor BLACK = registerValid("black");
    public static final ComponentColor DARK_BLUE = registerValid("dark_blue");
    public static final ComponentColor DARK_GREEN = registerValid("dark_green");
    public static final ComponentColor DARK_AQUA = registerValid("dark_aqua");
    public static final ComponentColor DARK_RED = registerValid("dark_red");
    public static final ComponentColor DARK_PURPLE = registerValid("dark_purple");
    public static final ComponentColor GOLD = registerValid("gold");
    public static final ComponentColor GRAY = registerValid("gray");
    public static final ComponentColor DARK_GRAY = registerValid("dark_gray");
    public static final ComponentColor BLUE = registerValid("blue");
    public static final ComponentColor GREEN = registerValid("green");
    public static final ComponentColor AQUA = registerValid("aqua");
    public static final ComponentColor RED = registerValid("red");
    public static final ComponentColor LIGHT_PURPLE = registerValid("light_purple");
    public static final ComponentColor YELLOW = registerValid("yellow");
    public static final ComponentColor WHITE = registerValid("white");

    private static final Pattern HEX_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Getter
    private final String text;

    private ComponentColor(String text) {
        this.text = text;
    }

    private ComponentColor(Color color) {
        this.text = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private static ComponentColor registerValid(String text) {
        VALID_NAMED_COLORS.add(text);
        return new ComponentColor(text);
    }

    public static @Nullable ComponentColor fromText(String text) {
        if (HEX_PATTERN.matcher(text).matches()) {
            return new ComponentColor(text);
        } else if (VALID_NAMED_COLORS.contains(text)) {
            return new ComponentColor(text);
        } else {
            return null;
        }
    }

    public static @NonNull ComponentColor fromColor(Color color) {
        return new ComponentColor(color);
    }
}
