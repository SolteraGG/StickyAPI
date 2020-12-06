/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.dumbdogdiner.stickyapi.common.util.BookUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Accessors(chain = true)
public class BookGenerator {
    public enum Generation {
        ORIGINAL(0),
        COPY_OF_ORIGINAL(1),
        COPY_OF_COPY(2),
        TATTERED(3);

        private Generation(int g) {
            genInt = g;
        }

        private final int genInt;

        public int asInt() {
            return genInt;
        }
    }

    private NBTTagList pages = new NBTTagList();


    @Setter
    @Getter
    private Generation generation = Generation.ORIGINAL;

    @Getter
    Material bookType;

    @Getter
    @Setter
    private String author;

    /**
     * Title of the book
     * -- SETTER --
     * Sets the name of the book, this may have ChatColors
     *
     * @param title The new value.
     * -- GETTER --
     * Returns the book title
     * @return String The book's title
     */
    @SuppressWarnings("JavaDoc")
    @Setter
    @Getter
    private String title;

    public BookGenerator(Material material) {
        if (material != Material.WRITABLE_BOOK && material != Material.WRITTEN_BOOK) {
            throw new IllegalArgumentException("Material must be WRITABLE_BOOK or WRITTEN_BOOK");
        }

        this.bookType = material;


    }
    /*
/give @p written_book{pages:['["",{"text":"Dumb Dog Diner MC\\nSurvival Handbook","bold":true,"italic":true,"color":"dark_purple"},{"text":"\\n\\n","color":"reset"},{"text":"Table of Contents:","bold":true,"italic":true,"underlined":true,"color":"dark_red"},{"text":"\\n","color":"reset"},{"text":"Page 2: ","color":"red"},{"text":"Rules\\n","color":"reset"},{"text":"Pg. 3: ","color":"red"},{"text":"Gameplay\\n","color":"reset"},{"text":"Pg. 5: ","color":"red"},{"text":"Trading\\n","color":"reset"},{"text":"Pg. 7: ","color":"red"},{"text":"Commands\\n","color":"reset"},{"text":"Pg. 9: ","color":"red"},{"text":"Trading\\nCommands\\n","color":"reset"},{"text":"Pg. 11: ","color":"red"},{"text":"Misc Commands\\n","color":"reset"},{"text":"Pg. 12: ","color":"red"},{"text":"Getting Help\\n","color":"reset"},{"text":"Pg. 14: ","color":"red"},{"text":"Credits","color":"reset"}]','["",{"text":"The Rules:","bold":true,"italic":true,"underlined":true,"color":"red"},{"text":"\\n1. Absolutely no\\ngriefing, theft, or\\nintentional distress!\\n2. No cheating/hacking\\nallowed. Optifine and\\nsimilar are okay.\\n3. No floating trees.\\n4. No fully-automatic\\nredstone farms.\\n5. No harassment,\\nbullying, or abuse.\\n6. No AFK fishing.","color":"reset"}]','["",{"text":"Gameplay:","bold":true,"italic":true,"underlined":true,"color":"dark_green"},{"text":"\\nThe world border for all worlds is 8,000\\nblocks from the\\ncenter. Because of\\nnow ","color":"reset"},{"text":"Nether Portal","color":"dark_red"},{"text":"\\ntravel works, you will\\nnot be able to light\\nthem past 1,000 blocks\\nfrom the center.\\nYou may build wherever you like! Try ","color":"reset"},{"text":"/rtp","color":"blue"},{"text":" to help find a spot!","color":"reset"}]','["",{"text":"This server uses a "},{"text":"Death Chest ","color":"red"},{"text":"system. Upon death, a campfire with your items will spawn at your death location. It will last\\nfor 10 minutes before\\nyour items drop on \\nthe ground!\\n\\nYou can have up to 3\\n","color":"reset"},{"text":"Death Chests","color":"red"},{"text":" at a time.","color":"reset"}]','["",{"text":"Trading:","bold":true,"italic":true,"underlined":true,"color":"dark_aqua"},{"text":"\\nWe use chest chops\\nfor player to trade\\ngood for ","color":"reset"},{"text":"Coins","color":"gold"},{"text":".\\n\\nCoins may be earned through selling goods\\nto other players,\\npaying/receiving\\nmoney for a service,\\nor by selling goods to\\nthe server itself.","color":"reset"}]','{"text":"Chest shops can buy\\nor sell items!\\n\\nTo make a shop, place a chest and\\nshift-click with the\\nitem you want to sell\\nor buy.\\n\\nRegular users may have up to 6 shops\\nat a time."}','["",{"text":"Commands:","bold":true,"italic":true,"underlined":true,"color":"red"},{"text":"\\nTab-complete is set\\nup to only show commands you have access to. Here are some useful ones:\\n\\n","color":"reset"},{"text":"/help ","color":"blue"},{"text":"- Opens the\\nhelp menu\\n","color":"reset"},{"text":"/servers ","color":"blue"},{"text":"- Opens the\\nserver menu\\n","color":"reset"},{"text":"/rules","color":"blue"},{"text":" - Grants this\\nbook","color":"reset"}]','["",{"text":"/options ","color":"blue"},{"text":"- Opens the player options menu\\n","color":"reset"},{"text":"/tpa ","color":"blue"},{"text":"- Sends a\\nteleport request to another player\\n","color":"reset"},{"text":"/rtp ","color":"blue"},{"text":"- Start a\\nrandom teleport\\n","color":"reset"},{"text":"/pw ","color":"blue"},{"text":"- Opens the playerwarps personal\\nhome menu\\n","color":"reset"},{"text":"/helpme ","color":"blue"},{"text":"- Pings an online staff member\\nfor assistance","color":"reset"}]','["",{"text":"Trading Commands:","bold":true,"italic":true,"underlined":true,"color":"dark_purple"},{"text":"\\n","color":"reset"},{"text":"/bal ","color":"blue"},{"text":"- Check your\\nbalance\\n","color":"reset"},{"text":"/pay ","color":"blue"},{"text":"- Pay another player\\n","color":"reset"},{"text":"/sell ","color":"blue"},{"text":"- Sell the item in your hand to the server\\n","color":"reset"},{"text":"/worth ","color":"blue"},{"text":"- Shows the\\nitem price when selling\\nto the server","color":"reset"}]','["",{"text":"/value","color":"blue"},{"text":" - Shows the\\naverage selling price\\nfor an item in\\nplayer shops","color":"reset"}]','["",{"text":"Extra Commands:","bold":true,"italic":true,"underlined":true,"color":"dark_aqua"},{"text":"\\n","color":"reset"},{"text":"/donate, /vip ","color":"blue"},{"text":"- Gives link to the server Patreon page\\n","color":"reset"},{"text":"/discord ","color":"blue"},{"text":"- Gives link to the server Discord\\n","color":"reset"},{"text":"/twitter, /twitch ","color":"blue"},{"text":"- Gives social media links\\n","color":"reset"},{"text":"/merch ","color":"blue"},{"text":"- Gives merch link\\n","color":"reset"},{"text":"/afk ","color":"blue"},{"text":"- Set your status to AFK","color":"reset"}]','["",{"text":"Getting Help:","bold":true,"italic":true,"underlined":true,"color":"light_purple"},{"text":"\\nIf you ever need help\\ngetting your way\\naround the server,\\nplease ask any staff!\\n","color":"reset"},{"text":"/helpme","color":"blue"},{"text":"\\n\\nPlease join the\\nDiscord server to get\\ntechnical help!\\n","color":"reset"},{"text":"/discord","color":"blue"}]','["",{"text":"Please report any\\nbugs, griefing, etc.\\nthat you come across\\nor are a victim of. A\\nstaff member will be\\nhappy to assist you.\\n\\nThe help menu has shortcuts that make getting help easy!\\n"},{"text":"/help","color":"blue"}]','["",{"text":"Credits:","bold":true,"italic":true,"underlined":true,"color":"gold"},{"text":"\\n","color":"reset"},{"text":"dddMC ","bold":true,"color":"dark_purple"},{"text":"is fully funded\\nthrough player donations. Please consider helping support us on Patreon!\\n","color":"reset"},{"text":"/donate /patreon","color":"blue"},{"text":"\\n\\nPatrons will receive a\\nVIP rank. More ranks\\nwill be added in the\\nfuture.","color":"reset"}]','["",{"text":"We hope you enjoy\\nyour time playing\\non the server!\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n- Stixil, "},{"text":"dddMC 2020","bold":true,"color":"dark_purple"}]'],title:"&ddddMC Survival Handbook",author:Stixil}

 */

    /**
     * Escape characters for colors, etc...
     * I guess we can just.... use the &amp;-* codes or something, we can use chatcolors somehow
     * Beyond that, we can use markdown???
     */

    public BookGenerator addPages(JsonObject... pages) {
        for (JsonObject page : pages) {
            addPage(page);
        }
        return this;
    }

    public BookGenerator addPage(JsonObject page) {
        if(!isFull()){
            pages.add(NBTTagString.create(page.toString()));
        } else {
            throw new IllegalStateException("Book is overfilled");
        }
        return this;
    }



    public ItemStack toItemStack(int qty) {
        NBTTagCompound root = new NBTTagCompound();
        if (bookType == Material.WRITTEN_BOOK) {
            root.setString("title", title);
            root.setString("author", author);
            root.setInt("generation", generation.asInt());
        }
        root.set("pages", pages);

        net.minecraft.server.v1_16_R3.ItemStack nmsBook = CraftItemStack.asNMSCopy(new ItemStack(bookType, qty));
        nmsBook.setTag(root);
        return CraftItemStack.asBukkitCopy(nmsBook);
    }


    public float percentFull() {
        return (float)pages.size() / (float)BookUtil.PAGES_PER_BOOK;
    }


    public boolean isFull() {
        return percentFull() >= 1.0f;
    }


}
