/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.dumbdogdiner.stickyapi.common.util.BookUtil;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Accessors(chain = true)
public class BookGenerator {
    private JsonArray pages = new JsonArray();
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


    private static final Gson G = new GsonBuilder().create();

    public BookGenerator(Material material) {
        if (material != Material.WRITABLE_BOOK && material != Material.WRITTEN_BOOK) {
            throw new IllegalArgumentException("Material must be WRITABLE_BOOK or WRITTEN_BOOK");
        }

        this.bookType = material;
    }


    /**
     * Creates a new BookGenerator from a given JsonObject, representing a book, which can
     * be obtained in whatever way you want.
     * @param bookObject A JSON object containing a JsonArray of valid Pages; If a {@link Material#WRITTEN_BOOK} is desired, it must contain a title, author, and optionally, a Generation (an integer with the value of 0, 1, 2, or 3, with 0 being the default)
     * @return a {@link BookGenerator} with the specified pages; if a title and author are both specified, it will be a {@link Material#WRITTEN_BOOK}, otherwise it will be a {@link Material#WRITABLE_BOOK}
     */
    public static BookGenerator fromJson(JsonObject bookObject) {
        BookGenerator b;

        if(bookObject.has("author") && bookObject.has("title")) {
            b = new BookGenerator(Material.WRITTEN_BOOK);
            b.author = bookObject.get("author").getAsString();
            b.title = bookObject.get("title").getAsString();
            if(bookObject.has("generation"))
                switch (bookObject.get("generation").getAsInt()){
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
        } else {
            b = new BookGenerator(Material.WRITABLE_BOOK);
        }
        b.pages = bookObject.get("pages").getAsJsonArray();
        return b;
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
        Preconditions.checkState(!isFull(), "Cannot add page, the book is overfilled!");
        pages.add(page);
        return this;
    }

    /**
     * Build a book from this generator.
     * @param qty Quantity of the item stack.
     * @return an {@link ItemStack} of the book, with pages and all other data
     */
    public ItemStack toItemStack(int qty) {
        Preconditions.checkArgument(qty > 0 && qty <= 16, "Invalid quantity specified, qty should be greater than 0 and less than or equal to 16, but was " + qty);
        Preconditions.checkState(pages.size() > 0, "Cannot generate book with no pages");
        Preconditions.checkState(pages.size() < BookUtil.PAGES_PER_BOOK, "Cannot generate book with an invalid number of pages (must be less than " + BookUtil.PAGES_PER_BOOK + ")");
        ItemStack stack = new ItemStack(bookType, qty);

        BookMeta meta = (BookMeta) stack.getItemMeta();

        if (bookType == Material.WRITTEN_BOOK) {
            meta.setTitle(title != null ? title : "");
            meta.setAuthor(author != null ? author : "");
            meta.setGeneration(generation);
        }

        stack = Bukkit.getUnsafe().modifyItemStack(stack, generateNbtString());

        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * @return The percentage of pages allowed that are used by this book.
     * TODO: Does not account for characters per page or packet size at this time.
     */
    public float percentFull() {
        return (float)pages.size() / (float) BookUtil.PAGES_PER_BOOK;
    }

    /**
     * TODO: Does not account for characters per page or packet size at this time.
     * @return True if the book is full.
     */
    public boolean isFull() {
        return percentFull() >= 1.0f;
    }


    /**
     * Uses a {@link StringJoiner} to convert pages JsonArray to the weird NBT list
     * @return {@link String} with NBT of the pages
     */
    @VisibleForTesting
    public String generateNbtString() {
        StringJoiner NBT = new StringJoiner("','", "{pages:['", "']}");
        pages.forEach(jsonElement -> NBT.add(G.toJson(jsonElement)/*.replace("'", "\\\"")*/));
        return NBT.toString().replace("\\n","\n").replace("\n","\\\\n");//TODO fix the new lines where they are originally
    }


}
