/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.common.util.BookUtil;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Collections;
import java.util.StringJoiner;

/**
 * This class allows for easy construction of a writable book, which is unfortunately much
 * simpler than a written/signed book, either manually in code, or by loading a JSON object
 *
 * @since 2.1.0
 */

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Accessors(chain = true)
public class BookAndQuillBuilder {
    private JsonArray pages = new JsonArray();
    private static final Gson G = new GsonBuilder().create();

    @Setter
    @Getter
    private @Nullable String displayName;

    @Setter
    @Getter
    private @Nullable String lore;

    public BookAndQuillBuilder() {

    }


    /**
     * Creates a new BookGenerator from a given JsonObject, representing a book, which can
     * be obtained in whatever way you want.
     *
     * @param bookObject A JSON object containing a JsonArray of valid Pages; If a {@link Material#WRITTEN_BOOK} is desired, it must contain a title, author, and optionally, a Generation (an integer with the value of 0, 1, 2, or 3, with 0 being the default)
     * @return a {@link BookAndQuillBuilder} with the specified pages; if a title and author are both specified, it will be a {@link Material#WRITTEN_BOOK}, otherwise it will be a {@link Material#WRITABLE_BOOK}
     */
    public static @NotNull BookAndQuillBuilder fromJson(@NotNull JsonObject bookObject) {
        BookAndQuillBuilder b = new BookAndQuillBuilder();
        b.pages = bookObject.get("pages").getAsJsonArray();
        return b;
    }

    /**
     * Add some pages to the book. May fail with {@link IllegalStateException} if the book is full or fills up.
     *
     * @param pages Pages to add
     * @return This object, for chaining
     */
    public @NotNull BookAndQuillBuilder addPages(String @NotNull ... pages) {
        for (String page : pages) {
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
    public @NotNull BookAndQuillBuilder addPage(@NotNull String page) {
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
        Preconditions.checkState(pages.size() < BookUtil.PAGES_PER_BOOK, "Cannot generate book with an invalid number of pages (must be less than " + BookUtil.PAGES_PER_BOOK + ")");
        ItemStack stack = new ItemStack(Material.WRITABLE_BOOK, qty);

        stack = Bukkit.getUnsafe().modifyItemStack(stack, generatePagesNBT());

        ItemMeta im = stack.getItemMeta();
        if (displayName != null)
            im.setDisplayName(StringUtil.formatChatCodes(displayName));

        if (lore != null)
            im.setLore(Collections.singletonList(lore));
        stack.setItemMeta(im);

        return stack;
    }

    /**
     * @return The percentage of pages allowed that are used by this book.
     * TODO: Does not account for characters per page or packet size at this time.
     */
    public float percentFull() {
        return (float) pages.size() / (float) BookUtil.PAGES_PER_BOOK;
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
        pages.forEach(jsonElement ->
                NBT.add(G.toJson(jsonElement)
                        // Because minecraft json is a hack on a hack.....
                        .replace("\n", "\\n")
                        .replace("\\", "\\\\"))

        );
        return NBT.toString();
    }
}
