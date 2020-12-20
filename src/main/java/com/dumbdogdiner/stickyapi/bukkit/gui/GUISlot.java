/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * A slot in a {@link GUI} object.
 */
@AllArgsConstructor
public class GUISlot {
    /** The item stack in this slot */
    @Getter
    private final @NotNull ItemStack itemStack;

    /** An optional tag that be used to identify the item in {@link GUI#onInventoryClick(InventoryClickEvent, String)} */
    @Getter
    private final @Nullable String tag;

    /** An optional procedure to run when this item is clicked, receives the {@link InventoryClickEvent} */
    @Getter
    private final @Nullable BiConsumer<InventoryClickEvent, GUI> action;

    /**
     * Create a copy of this GUISlot.
     * @return A copy of this slot.
     */
    public @NotNull GUISlot duplicate() {
        return new GUISlot(itemStack.clone(), tag, action);
    }
}
