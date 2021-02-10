/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;
import java.util.zip.CRC32;


// TODO: consider https://commons.apache.org/proper/commons-validator/apidocs/org/apache/commons/validator/routines/checkdigit/LuhnCheckDigit.html instead??
final class Luhn {
    private Luhn() {
    }

    /**
     * Checks if the card is valid
     * 
     * @param card {@link String} card number
     * @return result {@link boolean} true of false
     */
    public static boolean luhnCheck(@Nullable String card) {
        if (card == null || card.length() == 0)
            return false;

        char checkDigit = card.charAt(card.length() - 1);
        @Nullable String digit = calculateCheckDigit(card.substring(0, card.length() - 1));
        return checkDigit == digit.charAt(0);
    }

    /**
     * Calculates the last digits for the card number received as parameter
     * 
     * @param card {@link String} number
     * @return {@link String} the check digit
     */
    public static @Nullable String calculateCheckDigit(@Nullable String card) {
        if (card == null)
            return null;

        String digit;
        /* convert to array of int for simplicity */
        int @NotNull [] digits = new int[card.length()];

        for (int i = 0; i < card.length(); i++)
            digits[i] = Character.getNumericValue(card.charAt(i));

        /* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2) {
            digits[i] += digits[i];

            /* taking the sum of digits grater than 10 - simple trick by substract 9 */
            if (digits[i] >= 10)
                digits[i] = digits[i] - 9;
        }

        int sum = 0;
        for (int i = 0; i < digits.length; i++)
            sum += digits[i];

        /* multiply by 9 step */
        sum = sum * 9;

        /* convert to string to be easier to take the last digit */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }
}

/**
 * Class for generating small semi-unique IDs
 */
public class ShortID {

    private String id;

    public ShortID() {
        @NotNull Random r = new Random();
        this.id = generate(r.nextInt()).toString();
    }

    private ShortID(String name) {
        this.id = name;
    }

    public String toString() {
        return id;
    }

    /**
     * Create a ShortID from a string
     * 
     * @param name Returns a ShortID object from the string inputted
     * @throws IllegalArgumentException If name does not conform to the string
     *                                  representation as described in toString
     * @return {@link ShortID}
     */
    public static @NotNull ShortID fromString(@NotNull String name) throws IllegalArgumentException {
        if (!validateID(name))
            throw new IllegalArgumentException("The provided String is not a valid Luhn-parsable ShortID");
        return new ShortID((name));
    }

    private static @NotNull ShortID generateBase(int key) {
        Validate.notNull(key, "key cannot be null");
        @NotNull CRC32 crc = new CRC32();

        crc.update((int) System.nanoTime());
        // Some kind of semi-unique identifier (like last database row insertion)
        crc.update(key);

        // Get the hash
        @Nullable String crc32hash = Long.toHexString(crc.getValue());
        // Calculate the Luhn check digit
        crc32hash += Luhn.calculateCheckDigit(crc32hash);
        // return.
        return new ShortID(crc32hash.toUpperCase());
    }

    /**
     * Generate a short semi-unique ID
     * 
     * @param key An identifier to start the ID with (usually the position in a SQL
     *            table)
     * @return a Luhn-passable identifier
     */
    public static @NotNull ShortID generate(@NotNull int key) {
        return generateBase(key);
    }

    /**
     * Generate a short semi-unique ID
     * 
     * @return a Luhn-passable identifier
     */
    public static @NotNull ShortID generate() {
        return generateBase(Integer.valueOf(UUID.randomUUID().toString().replace("[A-z\\-]", "")));
    }

    /**
     * Validate that a string passes the Luhn test
     * 
     * @param shortId a string to test against the Luhn algorithm
     * @return Whether the string passes the Luhn test
     */
    public static boolean validateID(@NotNull String shortId) {
        Validate.notNull(shortId, "shortId cannot be null");
        return Luhn.luhnCheck(shortId);
    }
}
