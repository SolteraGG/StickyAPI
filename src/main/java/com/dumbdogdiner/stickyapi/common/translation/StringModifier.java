package com.dumbdogdiner.stickyapi.common.translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Takes a base string and modifies it with lambda expressions.
 */
public class StringModifier implements Cloneable {
    private String content;

    private final List<Function<String, String>> modifiers = new ArrayList<>();

    /**
     * Construct a new string modifier without parsing in a string to modify.
     */
    public StringModifier() {}

    /**
     * Construct a new StringModifier, providing default modifiers.
     * @param modifiers Default modifiers to add to this StringModifier.
     */
    @SafeVarargs
    public StringModifier(Function<String, String>... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
    }

    /**
     * Construct a new StringModifier, providing the initial content.
     * @param content The initial content of the modifier
     */
    public StringModifier(String content) {
        this.content = content;
    }

    /**
     * Get the modified string.
     * @return {@link String}
     */
    public final String get() {
        return this.content;
    }

    /**
     * Apply the given modifier to this string.
     * @param modifier The modifier to apply
     * @return {@link StringModifier}
     */
    public final StringModifier apply(Function<String, String> modifier) {
        this.content = modifier.apply(this.content);
        return this;
    }

    /**
     * Apply the given modifiers to this string.
     * @param modifiers The modifiers to apply
     * @return {@link StringModifier}
     */
    @SafeVarargs
    public final StringModifier apply(Function<String, String>[]... modifiers) {
        for (var modifier : modifiers) {
            this.apply(modifier);
        }
        return this;
    }

    /**
     * Add the given modifier to this object.
     * @param modifier Thie modifier to apply
     * @return {@link StringModifier}
     */
    public final StringModifier add(Function<String, String> modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    /**
     * Add the given modifiers to this object.
     * @param modifiers The modifiers to apply
     * @return {@link StringModifier}
     */
    @SafeVarargs
    public final StringModifier add(Function<String, String>... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    /**
     * Apply all modifiers on this object to the target string.
     * @param content The content to evaluate the modifiers on
     * @return {@link String}
     */
    public String applyAll(String content) {
        this.content = content;
        for (var modifier : this.modifiers) {
            this.apply(modifier);
        }
        return this.content;
    }

    /**
     * Apply all modifiers to the current content.
     * @return {@link String}
     */
    public String applyAll() {
        if (this.content == null) {
            throw new RuntimeException("Cannot invoke StringModifier.applyAll() when no content was specified");
        }
        return this.applyAll(this.content);
    }

    /**
     * Replace the target content with the specified string.
     * @param before The content to match
     * @param after The content to replace the match with
     * @return {@link StringModifier}
     */
    public StringModifier replace(String before, String after) {
        this.content = this.content.replace(before, after);
        return this;
    }
}
