/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.common.util.TextUtil;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Collections;
import java.util.StringJoiner;

/**
 * This class allows for easy construction of a Written Book, whether through JSON objects and manual
 * creation, or by loading a full JSON object from a file or other source
 *
 * @since 2.1.0
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
@Accessors(chain = true)
public class WrittenBookBuilder {
    private @NotNull JsonArray pages = new JsonArray();
    /**
     * The generation of the book.
     */
    @Getter
    @Setter
    private @NonNull BookMeta.@NotNull Generation generation = BookMeta.Generation.ORIGINAL;

    /**
     * The author of the book. Must not be null if the book is written.
     */
    @Getter
    @Setter
    private @NotNull String author = StringUtil.randomObfuscatedString(1, 17, 4);

    /**
     * The title of the book, can be formatted using color codes.
     */
    @Getter
    @Setter
    private @NotNull String title = StringUtil.randomObfuscatedString(1, 17, 6);

    /**
     * Lore/hover text for the item
     */
    @Getter
    @Setter
    private @Nullable String lore;

    private static final Gson G = new GsonBuilder()
            // Make sure things aren't weirdly escaped, may need to turn this back on
            .disableHtmlEscaping()
            .create();

    public WrittenBookBuilder() {

    }

    /**
     * Creates a new BookGenerator from a given JsonObject, representing a book, which can
     * be obtained in whatever way you want.
     *
     * @param bookObject A JSON object containing a JsonArray of valid Pages; If a {@link Material#WRITTEN_BOOK} is desired, it must contain a title, author, and optionally, a Generation (an integer with the value of 0, 1, 2, or 3, with 0 being the default)
     * @return a {@link WrittenBookBuilder} with the specified pages; if a title and author are both specified, it will be a {@link Material#WRITTEN_BOOK}, otherwise it will be a {@link Material#WRITABLE_BOOK}
     */
    public static @NotNull WrittenBookBuilder fromJson(@NotNull JsonObject bookObject) {
        Preconditions.checkArgument(bookObject.has("pages") && bookObject.get("pages").isJsonArray(), "The provided JSON object must have a pages array!");
        WrittenBookBuilder b = new WrittenBookBuilder();
        if (bookObject.has("author"))
            b.author = bookObject.get("author").getAsString();
        if (bookObject.has("title"))
            b.title = bookObject.get("title").getAsString();
        if (bookObject.has("generation"))
            switch (bookObject.get("generation").getAsInt()) {
                case 1:
                    b.generation = BookMeta.Generation.COPY_OF_ORIGINAL;
                    break;
                case 2:
                    b.generation = BookMeta.Generation.COPY_OF_COPY;
                    break;
                case 3:
                    b.generation = BookMeta.Generation.TATTERED;
                    break;
                case 0:
                default:
                    b.generation = BookMeta.Generation.ORIGINAL;
            }

        b.pages = bookObject.get("pages").getAsJsonArray();
        return b;
    }

    /**
     * Add some pages to the book. May fail with {@link IllegalStateException} if the book is full or fills up.
     *
     * @param pages Pages to add
     * @return This object, for chaining
     */
    public @NotNull WrittenBookBuilder addPages(JsonObject @NotNull ... pages) {
        for (JsonObject page : pages) {
            addPage(page);
        }
        return this;
    }

    /**
     * Add a page to the book. May fail with {@link IllegalStateException} if the book is full.
     *
     * @param page The page to add
     * @return This object, for chaining
     */
    public @NotNull WrittenBookBuilder addPage(@NotNull JsonObject page) {
        Preconditions.checkState(!isFull(), "Cannot add page, the book is overfilled!");
        pages.add(page);
        return this;
    }

    /**
     * Build a book from this generator.
     *
     * @param qty Quantity of the item stack.
     * @return an {@link ItemStack} of the book, with pages and all other data
     */
    @SuppressWarnings("deprecation")
    public @NotNull ItemStack toItemStack(int qty) {
        Preconditions.checkArgument(qty > 0 && qty <= 16, "Invalid quantity specified, qty should be greater than 0 and less than or equal to 16, but was " + qty);
        Preconditions.checkState(pages.size() > 0, "Cannot generate book with no pages");
        Preconditions.checkState(pages.size() < TextUtil.PAGES_PER_BOOK, "Cannot generate book with an invalid number of pages (must be less than " + TextUtil.PAGES_PER_BOOK + ")");
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, qty);

        stack = Bukkit.getUnsafe().modifyItemStack(stack, generatePagesNBT());

        BookMeta meta = (BookMeta) stack.getItemMeta();
        //noinspection ConstantConditions
        meta.setTitle(title != null ? StringUtil.formatChatCodes(title) : "");
        //noinspection ConstantConditions
        meta.setAuthor(author != null ? StringUtil.formatChatCodes(author) : "");
        meta.setGeneration(generation);
        if (lore != null)
            meta.setLore(Collections.singletonList(StringUtil.formatChatCodes(lore)));
        stack.setItemMeta(meta);

        return stack;
    }

    /**
     * @return The percentage of pages allowed that are used by this book.
     * TODO: Does not account for characters per page or packet size at this time.
     */
    public float percentFull() {
        return (float) pages.size() / (float) TextUtil.PAGES_PER_BOOK;
    }

    /**
     * TODO: Does not account for characters per page or packet size at this time.
     *
     * @return True if the book is full.
     */
    public boolean isFull() {
        return percentFull() >= 1.0f;
    }

    /**
     * Uses a {@link StringJoiner} to convert pages JsonArray to the weird NBT list
     *
     * @return {@link String} with NBT of the pages
     */
    @VisibleForTesting
    public @NotNull String generatePagesNBT() {
        StringJoiner NBT = new StringJoiner(",", "{pages:[", "]}");
        pages.forEach(jsonElement -> {
            if (jsonElement instanceof JsonArray) {
                JsonArray newArr = new JsonArray();
                newArr.add("");
                newArr.addAll(jsonElement.getAsJsonArray());
                jsonElement = newArr;
            }
            NBT.add("'" +
                    G.toJson(jsonElement)
                            // Because minecraft json is a hack on a hack.....

                            .replaceAll("(?<!\\\\)'", "\\\\'") // Escape single quotes
                            .replace("\n", "\\n") // Replace new lines with escaped versions
                            .replaceAll("\\+n", "\\\\n") // Fix formatting of any escaped new lines already in there
                    + "'");

        });
        return NBT.toString();
    }
}
