/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.dumbdogdiner.stickyapi.common.util.BookUtil;
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
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Accessors(chain = true)
public class BookGenerator {
    private final JsonArray pages = new JsonArray();
    /** The generation of the book. */
    @Getter @Setter
    private @NonNull BookMeta.Generation generation = BookMeta.Generation.ORIGINAL;
    /** The material of the book. */
    @Getter
    private final Material bookType;
    /** The author of the book. Must not be null if the book is written. */
    @Getter @Setter
    private @Nullable String author;
    /** The title of the book, can be formatted using color codes. */
    @Getter @Setter
    private String title;

    public BookGenerator(Material material) {
        if (material != Material.WRITABLE_BOOK && material != Material.WRITTEN_BOOK) {
            throw new IllegalArgumentException("Material must be WRITABLE_BOOK or WRITTEN_BOOK");
        }

        this.bookType = material;
    }

    /**
     * Add some pages to the book. May fail with {@link IllegalStateException} if the book is full or fills up.
     * @param pages Pages to add
     * @return This object, for chaining
     */
    public BookGenerator addPages(JsonObject... pages) {
        for (JsonObject page : pages) {
            addPage(page);
        }
        return this;
    }

    /**
     * Add a page to the book. May fail with {@link IllegalStateException} if the book is full.
     * @param page The page to add
     * @return This object, for chaining
     */
    public BookGenerator addPage(JsonObject page) {
        if(!isFull()){
            pages.add(page.toString());
        } else {
            throw new IllegalStateException("Book is overfilled");
        }
        return this;
    }

    /**
     * Build a book from this generator.
     * @param qty Quantity of the item stack.
     * @return {@link ItemStack}
     */
    public ItemStack toItemStack(int qty) {
        ItemStack stack = Bukkit.getUnsafe().modifyItemStack(new ItemStack(bookType), "{pages: " + pages.toString() + "}");

        if (bookType == Material.WRITTEN_BOOK) {
            BookMeta meta = (BookMeta) stack.getItemMeta();
            meta.setTitle(title);
            meta.setAuthor(author);
            meta.setGeneration(generation);
            stack.setItemMeta(meta);
        }

        return stack;
    }

    /**
     * @return The percentage of pages allowed that are used by this book.
     */
    public float percentFull() {
        return (float)pages.size() / (float)BookUtil.PAGES_PER_BOOK;
    }

    /**
     * @return True if the book is full.
     */
    public boolean isFull() {
        return percentFull() >= 1.0f;
    }


}
