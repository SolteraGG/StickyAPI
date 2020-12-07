/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.bukkit.item.generator.BookGenerator;
import com.dumbdogdiner.stickyapi.common.book.commonmarkextensions.JsonComponent;
import com.dumbdogdiner.stickyapi.common.book.commonmarkextensions.JsonComponentWriter;
import com.dumbdogdiner.stickyapi.common.book.commonmarkextensions.MCColorFormatDelimiterProcessor;
import com.dumbdogdiner.stickyapi.common.book.commonmarkextensions.MarkdownJsonRenderer;
import com.dumbdogdiner.stickyapi.common.util.BookUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.commonmark.node.Document;
import org.commonmark.parser.Parser;

import java.io.*;
import java.util.List;

public class RuleBook {
    //TODO MOVE TO CONST
    private static final String RULEBOOK_PATH = "plugin name i guess? /rulebook.md";

    // TODO where should we expect to find rulebook.md? using server root directory for now
    private static File getRulebookPath() {
        return new File(Bukkit.getWorldContainer(), "rulebook.md");
    }

    public static ItemStack generateDefault() throws IOException {
        try (var reader = new InputStreamReader(new FileInputStream(getRulebookPath()))) {
            return generate(reader, "§ddddMCSurvival Handbook", "Stixil");
        }
    }

    public static ItemStack generate(Reader reader, String title, String author) throws IOException {
        var cmparser = Parser.builder()
                .customDelimiterProcessor(new MCColorFormatDelimiterProcessor())
                .build();
        var bookRoot = (Document) cmparser.parseReader(reader);
        var bookGenerator = new BookGenerator(Material.WRITTEN_BOOK);
        BookUtil.splitDocumentByHeadings(bookRoot, 2)
                .stream().map(document -> {
                    var component = new JsonComponent();
                    var writer = new JsonComponentWriter(component);
                    new MarkdownJsonRenderer(writer).render(document);
                    return component;
                }).map(BookUtil::splitBookPages).flatMap(List::stream).forEach(page -> {
                    bookGenerator.addPage(page.toJson());
                });
        bookGenerator.setTitle(title);
        bookGenerator.setAuthor(author);
        return bookGenerator.toItemStack(1);
    }
}
