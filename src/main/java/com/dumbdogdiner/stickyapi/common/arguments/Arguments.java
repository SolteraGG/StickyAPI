/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
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
 *  
 */

package com.dumbdogdiner.stickyapi.common.arguments;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.dumbdogdiner.stickyapi.common.util.Debugger;
import com.dumbdogdiner.stickyapi.common.util.NumberUtil;
import com.dumbdogdiner.stickyapi.common.util.TimeUtil;

import lombok.Getter;

/**
 * Utility class for handling command arguments.
 */
public class Arguments {
    @Getter private ArrayList<String> unparsedArgs;
    private HashMap<String, String> parsedArgs = new HashMap<>();

    private int position = 0;
    private boolean valid = true;
    private Debugger debug = new Debugger(getClass());

    public void invalidate(String name) {
        debug.print("Invalidated by argument " + name);
        valid = false;
    }
 
    /**
     * Are these arguments valid?
     * Returns true if are valid
     */
    public boolean valid() {
        return valid;
    }

    /**
     * Construct a new argument class with the given input.
     * @param args Arguments to parse
     */
    public Arguments(String[] args) {
        unparsedArgs = new ArrayList<String>(Arrays.asList(args));
    }

    /**
     * Create an optional flag.
     * @param name The name of this flag
     * @param flag The flag to register
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalFlag(String name, String flag) {
        debug.print("Looking for optional flag " + name + "...");
        int index = unparsedArgs.indexOf(flag);
        if (index == -1) {
            debug.print("Could not find flag");
            return this;
        }

        debug.print("Found flag at position " + String.valueOf(index) + " - new args size = " + String.valueOf(unparsedArgs.size()));

        parsedArgs.put(name, unparsedArgs.get(index));
        unparsedArgs.remove(index);

        return this;
    }

    /**
     * Create a required flag.
     * @param name The name of this flag
     * @param flag The flag to register
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredFlag(String name, String flag) {
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


    /**
     * Create a required string argument.
     * @param name The name of this string
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalString(String name) {
        debug.print("Looking for optional string " + name + "...");
        if (unparsedArgs.size() > position) {
            parsedArgs.put(name, unparsedArgs.get(position));
            unparsedArgs.remove(position);
            debug.print("Found string at position " + String.valueOf(position) + " - new args size = " + String.valueOf(unparsedArgs.size()));
        } else 
            debug.print("Could not find string");

        return this;
    }

    /**
     * Create a required string argument.
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
    
    /**
     * Create an optional sentence argument, with its length defaulting to the remaining length of
     * current unparsed arguments.
     * @param name The name of this sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalSentence(String name) {
        debug.print("Using default length: " + String.valueOf(unparsedArgs.size() - position));
        return optionalSentence(name, unparsedArgs.size() - position);
    }

    /**
     * Create an optional sentence with the given length.
     * @param name The name of the sentence to createa
     * @param length The length of the sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalSentence(String name, int length) {
        int end = position + length;
        debug.print("Looking for optional sentence - start = " + String.valueOf(position) + ", end = " + String.valueOf(end) + ", length = " + String.valueOf(length));
    
        if (position >= end) {
            debug.print("Start cannot be greater than or equal to end");
            return this;
        }
        
        if (unparsedArgs.size() < position + length) {
            debug.print("Could not find sentence of appropriate length (args are size " + String.valueOf(position)
                    + ")");
            return this;
        }

        String concatenated = String.join(" ", Arrays.copyOfRange(unparsedArgs.toArray(new String[unparsedArgs.size()]), position, end));
        parsedArgs.put(name, concatenated);

        for (int s = position; s != end; s++)
            unparsedArgs.remove(position);
        
        debug.print("Found sentence of length " + String.valueOf(length) + " - new args size = " + String.valueOf(unparsedArgs.size()));

        return this;
    }

    /**
     * Create a required sentence argument, its length defaulting to that of any remaining unparsed arguments.
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredSentence(String name) {
        debug.print("Using default length: " + String.valueOf(unparsedArgs.size() - position));
        return requiredSentence(name, unparsedArgs.size() - position);
    }

    /**
     * Create a required sentence argument with the given length.
     * @param name Name of the argument
     * @param length Maximum length in words of the sentence
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredSentence(String name, int length) {
        int end = position + length;
        debug.print("Looking for required sentence - start = " + String.valueOf(position) + ", end = " + String.valueOf(end) + ", length = " + String.valueOf(length));

        // Usually means there aren't enough args left
        if (position >= end) {
            debug.print("Start cannot be greater than or equal to end");
            invalidate(name);
            return this;
        }
        
        if (unparsedArgs.size() < position + length) {
            invalidate(name);
            debug.print("Could not find sentence of appropriate length (args are size " + String.valueOf(unparsedArgs.size()) + ") - marking as invalid");
            return this;
        }

        String concatenated = String.join(" ", Arrays.copyOfRange(unparsedArgs.toArray(new String[unparsedArgs.size()]), position, end));
        parsedArgs.put(name, concatenated);

        position += length;
        
        debug.print("Found sentence of length " + String.valueOf(length));

        return this;
    }

    /**
     * Create an optional timestamp argument.
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalTimestamp(String name) {
        debug.print("Looking for optional timestamp " + name + "...");
        if (unparsedArgs.size() > position && TimeUtil.toTimestamp(unparsedArgs.get(position)) != null) {
            parsedArgs.put(name, String.valueOf(TimeUtil.toTimestamp(unparsedArgs.get(position)).getTime()));
            unparsedArgs.remove(position);
            debug.print("Found timestamp at position " + String.valueOf(position) + " - new args size = " + String.valueOf(unparsedArgs.size()));
        } else 
            debug.print("Could not find timestamp");

        return this;
    }

    /**
     * Create an optional timestamp argument.
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredTimestamp(String name) {
        debug.print("Looking for required timestamp " + name + "...");
        if (unparsedArgs.size() > position && TimeUtil.toTimestamp(unparsedArgs.get(position)) != null) {
            parsedArgs.put(name, String.valueOf(TimeUtil.toTimestamp(unparsedArgs.get(position)).getTime()));
            position++;
            debug.print("Found timestamp at position " + String.valueOf(position) + " - new args size = "
                    + String.valueOf(unparsedArgs.size()));
        } else
            debug.print("Could not find timestamp");

        return this;
    }

    /**
     * Create an optional integer argument.
     * @param name Name of the argument
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalInt(String name) {
        debug.print("Looking for optional int " + name + "...");

        if (unparsedArgs.size() > position && NumberUtil.isNumeric(unparsedArgs.get(position))) {
            parsedArgs.put(name, unparsedArgs.get(position));
            unparsedArgs.remove(position);
            debug.print("Found int at position " + String.valueOf(position) + " - new args size = " + String.valueOf(unparsedArgs.size()));
        } else 
            debug.print("Could not find int");

        return this;
    }

    /**
     * Create a required integer
     * @param name The name of the integer to create
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredInt(String name) {
        debug.print("Looking for optional required " + name + "...");

        if (unparsedArgs.size() > position && NumberUtil.isNumeric(unparsedArgs.get(position))) {
            parsedArgs.put(name, unparsedArgs.get(position));
            position++;
            debug.print("Found int at position " + String.valueOf(position) + " - new args size = " + String.valueOf(unparsedArgs.size()));
        } else  {
            debug.print("Could not find int - marking as invalid");
            invalidate(name);
        }

        return this;
    }

    /**
     * Create an optional duration argument.
     * @param name The name of the duration to create
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments optionalDuration(String name) {
        debug.print("Looking for optional duration " + name + "...");

        if (unparsedArgs.size() > position && TimeUtil.duration(unparsedArgs.get(position)).isPresent()) {
            parsedArgs.put(name, unparsedArgs.get(position));
            unparsedArgs.remove(position);
            debug.print("Found duration at position " + String.valueOf(position) + " - new args size = " + String.valueOf(unparsedArgs.size()));
        } else 
            debug.print("Could not find duration");

        return this;
    }

    /**
     * Create a required duration argument.
     * @param name The name of the duration to create
     * @return {@link com.dumbdogdiner.stickyapi.common.arguments.Arguments}
     */
    public Arguments requiredDuration(String name) {
        debug.print("Looking for required duration " + name + "...");

        if (unparsedArgs.size() > position && TimeUtil.duration(unparsedArgs.get(position)).isPresent()) {
            parsedArgs.put(name, unparsedArgs.get(position));
              position++;
            debug.print("Found duration at position " + String.valueOf(position) + " - new args size = " + String.valueOf(unparsedArgs.size()));
        } else {
            debug.print("Could not find duration - marking as invalid");
            invalidate(name);
        }

        return this;
    }

    /**
     * Fetch a parsed argument from this arguments object.
     * <p>Returns the argument, if it exists
     * @param name The name of the argument to fetch
     */
    public String get(String name) {
        return parsedArgs.get(name);
    }

    /**
     * Fetch a timestamp.
     * <p>Returns the argument, if it exists
     * @param name The name of the timestamp to fetch
     */
    public Timestamp getTimestamp(String name) {
        if (parsedArgs.get(name) == null) {
            return null;
        }
        return new Timestamp(Long.valueOf(parsedArgs.get(name)));
    }

    /**
     * Fetch an integer.
     * <p>Returns the argument, if it exists
     * @param name The name of the integer to fetch\
     */
    public Integer getInt(String name) {
        try {
            return Integer.parseInt(parsedArgs.get(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Fetch a double.
     * <p>Returns the argument, if it exists
     * @param name The name of the double to fetch\
     */
    public Double getDouble(String name) {
        try {
            return Double.parseDouble(parsedArgs.get(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Fetch a long.
     * <p>Returns the argument, if it exists
     * @param name The name of the long to fetch\
     */
    public Long getLong(String name) {
        try {
            return Long.parseLong(parsedArgs.get(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Return whether an argument exists.
     * <p>Returns the argument, if it exists
     * @param name The name of the argument to check for
     */
    public Boolean exists(String name) {
        return parsedArgs.get(name) != null;
    }

    /**
     * Return whether a flag is set.
     * <p>Returns the argument, if it exists
     * @param name The name of the flag to fetch
     */
    public Boolean getFlag(String name) {
        return exists(name);
    }

    /**
     * Fetch a boolean value (usually a flag).
     * <p>Returns the argument, if it exists
     * @param name The name of the boolean to fetch
     */
    public Boolean getBoolean(String name) {
        return Boolean.valueOf(parsedArgs.get(name));
    }

    /**
     * Fetch a duration value.
     * <p>Returns the argument, if it exists
     * @param name The name of the duration to fetch
     */
    public Long getDuration(String name) {
        return TimeUtil.duration(parsedArgs.get(name)).get();
    }
}
