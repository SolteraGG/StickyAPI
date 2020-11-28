/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.arguments;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.dumbdogdiner.stickyapi.common.util.Debugger;
import com.dumbdogdiner.stickyapi.common.util.NumberUtil;
import com.dumbdogdiner.stickyapi.common.util.TimeUtil;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for handling command arguments.
 */
public class Arguments {
    @Getter
    private List<String> rawArgs;

    @Getter
    private ArrayList<String> unparsedArgs;
    private HashMap<String, String> parsedArgs = new HashMap<>();

    @Getter
    private String invalidatedBy;

    private int position = 0;
    private boolean valid = true;
    private final Debugger debug = new Debugger(getClass());

    public void invalidate(@NotNull String name) {
        debug.print("Invalidated by argument " + name);
        invalidatedBy = name;
        valid = false;
    }

    /**
     * Returns true if this Arguments instance is valid.
     * 
     * @return {@link java.lang.Boolean}
     */
    public boolean valid() {
        return valid;
    }

    /**
     * Construct a new argument class with the given input.
     * 
     * @param args Arguments to parse
     * @since 2.0
     */
    public Arguments(@NotNull List<String> args) {
        unparsedArgs = new ArrayList<String>(args);
        rawArgs = Collections.unmodifiableList(args);
    }

    /**
     * Create an optional flag.
     * 
     * @since 1.4.6
     * @param flag The name of this flag, and flag to register
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalFlag(@NotNull String flag) {
        return optionalFlag(flag, flag);
    }

    /**
     * Create an optional flag.
     * 
     * @param name The name of this flag
     * @param flag The flag to register
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalFlag(@NotNull String name, @NotNull String flag) {
        debug.print("Looking for optional flag " + name + "...");
        int index = unparsedArgs.indexOf(flag);
        if (index == -1) {
            debug.print("Could not find flag");
            return this;
        }

        debug.print("Found flag at position " + index + " - new args size = " + unparsedArgs.size());

        parsedArgs.put(name, unparsedArgs.get(index));
        unparsedArgs.remove(index);

        return this;
    }

    /**
     * Create a required flag.
     * 
     * @param name The name of this flag
     * @param flag The flag to register
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredFlag(@NotNull String name, @NotNull String flag) {
        debug.print("Looking for required flag " + name + "...");
        int index = unparsedArgs.indexOf(flag);
        if (index == -1) {
            invalidate(name);
            debug.print("Could not find flag - marking as invalid");
            return this;
        }

        debug.print("Found flag at position " + String.valueOf(index));

        parsedArgs.put(name, unparsedArgs.get(index));
        unparsedArgs.remove(index);

        return this;
    }

    private Arguments optionalStringImplementation(String name, String fallback) {
        debug.print("Looking for optional string " + name + "...");
        if (unparsedArgs.size() > position) {
            parsedArgs.put(name, unparsedArgs.get(position));
            unparsedArgs.remove(position);
            debug.print("Found string at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else {
            debug.print("Could not find string, using default value of " + fallback);
            parsedArgs.put(name, fallback);
        }

        return this;
    }

    /**
     * Create an optional string argument with a default value
     * 
     * @param name     The name of this string
     * @param fallback the default value you want for the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalString(@NotNull String name, @NotNull String fallback) {
        // noinspection ConstantConditions
        return optionalStringImplementation(name, fallback);
    }

    /**
     * Create an optional string argument.
     * 
     * @param name The name of this string
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalString(String name) {
        return optionalStringImplementation(name, null);
    }

    /**
     * Create a required string argument.
     * 
     * @param name The name of this string
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredString(String name) {
        debug.print("Looking for required string " + name + "...");

        if (unparsedArgs.size() > position) {
            parsedArgs.put(name, unparsedArgs.get(position));
            debug.print("Found string at position " + String.valueOf(position));
            position++;
        } else {
            debug.print("Could not find string - marking as invalid");
            invalidate(name);
        }

        return this;
    }

    private Arguments optionalSentenceImplementation(String name, String fallback, int length) {
        int end = position + length;
        debug.print("Looking for optional sentence - start = " + String.valueOf(position) + ", end = "
                + String.valueOf(end) + ", length = " + String.valueOf(length));

        if (position >= end) {
            debug.print("Start cannot be greater than or equal to end, using default value of " + fallback);
            parsedArgs.put(name, fallback);
            return this;
        }

        if (unparsedArgs.size() < position + length) {
            debug.print("Could not find sentence of appropriate length (args are size " + String.valueOf(position)
                    + ") using default value of " + fallback);
            parsedArgs.put(name, fallback);
            return this;
        }

        String concatenated = String.join(" ",
                Arrays.copyOfRange(unparsedArgs.toArray(new String[unparsedArgs.size()]), position, end));
        parsedArgs.put(name, concatenated);

        if (end > position) {
            unparsedArgs.subList(position, end).clear();
        }

        debug.print("Found sentence of length " + String.valueOf(length) + " - new args size = "
                + String.valueOf(unparsedArgs.size()));

        return this;
    }

    /**
     * Create an optional sentence argument, with its length defaulting to the
     * remaining length of current unparsed arguments.
     * 
     * @param name The name of this sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalSentence(@NotNull String name) {
        debug.print("Using default length: " + String.valueOf(unparsedArgs.size() - position));
        return optionalSentence(name, unparsedArgs.size() - position);
    }

    /**
     * Create an optional sentence argument, with its length defaulting to the
     * remaining length of current unparsed arguments.
     * 
     * @param name     The name of this sentence
     * @param fallback The fallback sentence to use if one is not provided
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalSentence(@NotNull String name, @Nullable String fallback) {
        debug.print("Using default length: " + String.valueOf(unparsedArgs.size() - position));
        return optionalSentence(name, fallback, unparsedArgs.size() - position);
    }

    /**
     * Create an optional sentence with the given length.
     * 
     * @param name     The name of the sentence to create
     * @param fallback The fallback sentence to use if one is not provided
     * @param length   The length of the sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalSentence(@NotNull String name, @Nullable String fallback, @NotNull int length) {
        if (fallback != null) {
            return optionalSentenceImplementation(name, fallback, length);
        } else {
            debug.print("Explicit fallback string of null attempted for parameter " + name + ", argument not added.");
            return this;
        }
    }

    /**
     * Create an optional sentence with the given length.
     * 
     * @param name   The name of the sentence to create
     * @param length The length of the sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalSentence(@NotNull String name, @NotNull int length) {
        optionalSentenceImplementation(name, null, length);
        return this;
    }

    /**
     * Create a required sentence argument, its length defaulting to that of any
     * remaining unparsed arguments.
     * 
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredSentence(@NotNull String name) {
        debug.print("Using default length: " + String.valueOf(unparsedArgs.size() - position));
        return requiredSentence(name, unparsedArgs.size() - position);
    }

    /**
     * Create a required sentence argument with the given length.
     * 
     * @param name   Name of the argument
     * @param length Maximum length in words of the sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredSentence(@NotNull String name, @NotNull int length) {
        int end = position + length;
        debug.print("Looking for required sentence - start = " + String.valueOf(position) + ", end = "
                + String.valueOf(end) + ", length = " + String.valueOf(length));

        // Usually means there aren't enough args left
        if (position >= end) {
            debug.print("Start cannot be greater than or equal to end");
            invalidate(name);
            return this;
        }

        if (unparsedArgs.size() < position + length) {
            invalidate(name);
            debug.print("Could not find sentence of appropriate length (args are size "
                    + String.valueOf(unparsedArgs.size()) + ") - marking as invalid");
            return this;
        }

        String concatenated = String.join(" ",
                Arrays.copyOfRange(unparsedArgs.toArray(new String[unparsedArgs.size()]), position, end));
        parsedArgs.put(name, concatenated);

        position += length;

        debug.print("Found sentence of length " + String.valueOf(length));

        return this;
    }

    /**
     * Create an optional timestamp argument. (e.g. 1w2d5s)
     * 
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalTimeString(@NotNull String name) {
        debug.print("Looking for optional timestamp " + name + "...");
        if (unparsedArgs.size() > position && TimeUtil.toTimestamp(unparsedArgs.get(position)) != null) {
            parsedArgs.put(name, String.valueOf(TimeUtil.toTimestamp(unparsedArgs.get(position)).getTime()));
            unparsedArgs.remove(position);
            debug.print("Found timestamp at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else
            debug.print("Could not find timestamp");

        return this;
    }

    /**
     * Create a required timestamp argument. (e.g. 1w2d5s)
     * 
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredTimeString(@NotNull String name) {
        debug.print("Looking for required timestamp " + name + "...");
        if (unparsedArgs.size() > position && TimeUtil.toTimestamp(unparsedArgs.get(position)) != null) {
            parsedArgs.put(name, String.valueOf(TimeUtil.toTimestamp(unparsedArgs.get(position)).getTime()));
            position++;
            debug.print("Found timestamp at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else {
            debug.print("Could not find timestamp");
            invalidate(name);
        }

        return this;
    }

    private Arguments optionalIntImplementation(@NotNull String name, @NotNull Integer fallback) {
        debug.print("Looking for optional integer " + name + "...");
        if (unparsedArgs.size() > position && NumberUtil.isNumeric(unparsedArgs.get(position))) {
            parsedArgs.put(name, unparsedArgs.get(position));
            position++;
            debug.print("Found int at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else {
            debug.print("Could not find int, using default value of " + fallback);
            parsedArgs.put(name, fallback.toString());
        }

        return this;
    }

    /**
     * Create an optional integer argument.
     * 
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalInt(@NotNull String name) {
        optionalIntImplementation(name, null);
        return this;
    }

    /**
     * Create an optional integer argument.
     * 
     * @param name     Name of the argument
     * @param fallback The fallback integer to use if one is not provided
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalInt(@NotNull String name, @NotNull Integer fallback) {
        if (fallback != null) {
            return optionalIntImplementation(name, fallback);
        } else {
            debug.print("Explicit fallback integer of null attempted for parameter " + name + ", argument not added.");
            return this;
        }
    }

    /**
     * Create a required integer
     * 
     * @param name The name of the integer to create
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredInt(@NotNull String name) {
        debug.print("Looking for optional required " + name + "...");

        if (unparsedArgs.size() > position && NumberUtil.isNumeric(unparsedArgs.get(position))) {
            parsedArgs.put(name, unparsedArgs.get(position));
            position++;
            debug.print("Found int at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else {
            debug.print("Could not find int - marking as invalid");
            invalidate(name);
        }

        return this;
    }

    /**
     * Create an optional duration argument.
     * 
     * @param name The name of the duration to create
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalDuration(@NotNull String name) {
        debug.print("Looking for optional duration " + name + "...");

        if (unparsedArgs.size() > position && TimeUtil.duration(unparsedArgs.get(position)).isPresent()) {
            parsedArgs.put(name, unparsedArgs.get(position));
            unparsedArgs.remove(position);
            debug.print("Found duration at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else
            debug.print("Could not find duration");

        return this;
    }

    /**
     * Create a required duration argument.
     * 
     * @param name The name of the duration to create
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredDuration(@NotNull String name) {
        debug.print("Looking for required duration " + name + "...");

        if (unparsedArgs.size() > position && TimeUtil.duration(unparsedArgs.get(position)).isPresent()) {
            parsedArgs.put(name, unparsedArgs.get(position));
            position++;
            debug.print("Found duration at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else {
            debug.print("Could not find duration - marking as invalid");
            invalidate(name);
        }

        return this;
    }

    /**
     * Fetch a parsed argument from this arguments object.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the argument to fetch
     * @return {@link java.lang.String}
     * @since 2.0
     */
    public String getString(@NotNull String name) {
        return parsedArgs.get(name);
    }

    /**
     * Get the HashMap of parsed arguments.
     * 
     * @return {@link java.util.HashMap}
     * @since 2.0
     */
    public HashMap<String, String> get() {
        return this.parsedArgs;
    }

    /**
     * Fetch a timestamp.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the timestamp to fetch
     * @return {@link java.sql.Timestamp}
     */
    public Timestamp getTimestamp(@NotNull String name) {
        if (parsedArgs.get(name) == null) {
            return null;
        }
        return new Timestamp(Long.parseLong(parsedArgs.get(name)));
    }

    /**
     * Fetch an integer.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the integer to fetch
     * @return {@link java.lang.Integer}
     */
    public Integer getInt(@NotNull String name) {
        try {
            return Integer.parseInt(parsedArgs.get(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Fetch a double.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the double to fetch
     * @return {@link java.lang.Double}
     */
    public Double getDouble(@NotNull String name) {
        try {
            return Double.parseDouble(parsedArgs.get(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Fetch a long.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the long to fetch
     * @return {@link java.lang.Long}
     */
    public Long getLong(@NotNull String name) {
        try {
            return Long.parseLong(parsedArgs.get(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Return whether an argument exists.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the argument to check for
     * @return {@link java.lang.Boolean}
     */
    public Boolean exists(@NotNull String name) {
        return parsedArgs.get(name) != null;
    }

    /**
     * Return whether a flag is set.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the flag to fetch
     * @return {@link java.lang.Boolean}
     */
    public Boolean getFlag(@NotNull String name) {
        return exists(name);
    }

    /**
     * Fetch a boolean value (usually a flag).
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the boolean to fetch
     * @return {@link java.lang.Boolean}
     */
    public Boolean getBoolean(@NotNull String name) {
        return Boolean.valueOf(parsedArgs.get(name));
    }

    /**
     * Fetch a duration value.
     * <p>
     * Returns the argument, if it exists
     * 
     * @param name The name of the duration to fetch
     * @return {@link java.lang.Long}
     */
    public Long getDuration(@NotNull String name) {
        return TimeUtil.duration(parsedArgs.get(name)).isPresent() ? TimeUtil.duration(parsedArgs.get(name)).get()
                : null;
    }

    /**
     * Fetch the last argument in the raw arguments list
     * @return {@link String} the final argument in the list
     */
    public String getLastArgument(){
        return rawArgs.get(rawArgs.size() - 1);
    }
}
