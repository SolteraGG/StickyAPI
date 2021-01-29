/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

/**
 * An event that is fired when text associated with this event is hovered over.
 */
public abstract class HoverEvent {
    private HoverEvent() {} // seal this class

    /**
     * Convert the HoverEvent into JSON representation used in-game.
     * @return {@link JsonElement}
     */
    public abstract @NonNull JsonElement toJson();

    /**
     * Create a copy of this HoverEvent.
     * @return {@link HoverEvent}
     */
    public abstract @NonNull HoverEvent duplicate();

    /**
     * When hovered over, show some text.
     */
    public final static class ShowText extends HoverEvent {
        /** The text to show when the associated component is hovered over. */
        @Getter
        private final @NonNull JsonComponent component;

        public ShowText(@NonNull JsonComponent component) {
            this.component = component;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "show_text");
            object.add("contents", component.toJson());
            return object;
        }

        @Override
        public ShowText duplicate() {
            return new ShowText(component);
        }
    }

    /**
     * When hovered over, show the tooltip of an item.
     */
    public final static class ShowItem extends HoverEvent {
        /** The item to reference for the tooltip. */
        @Getter
        private final @NonNull ItemStack item;
        /** Extra NBT to apply to this item stack. */
        @Getter
        private final @Nullable String extraNBT; // might be possible to get this from the item but would need lots of nms

        public ShowItem(@NonNull ItemStack item, @Nullable String extraNBT) {
            this.item = item;
            this.extraNBT = extraNBT;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "show_item");
            JsonObject contents = new JsonObject();
            contents.addProperty("id", item.getType().getKey().toString());
            if (item.getAmount() != 1) {
                contents.addProperty("count", item.getAmount());
            }
            if (extraNBT != null) {
                contents.addProperty("tag", extraNBT);
            }
            object.add("contents", contents);
            return object;
        }

        @Override
        public ShowItem duplicate() {
            return new ShowItem(item, extraNBT);
        }
    }

    /**
     * When hovered over, show data about an entity.
     */
    public final static class ShowEntity extends HoverEvent {
        /** Text to display as the name of the entity. */
        @Getter
        private final @Nullable JsonComponent name;
        /** The type of the entity. */
        @Getter
        private final @NonNull EntityType entityType;
        /** The ID of the entity. */
        @Getter
        private final @NonNull UUID entityId;

        public ShowEntity(@Nullable JsonComponent name, @NonNull EntityType entityType, @NonNull UUID entityId) {
            this.name = name;
            this.entityType = entityType;
            this.entityId = entityId;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "show_entity");
            JsonObject contents = new JsonObject();
            if (name != null) {
                contents.add("name", name.toJson());
            }
            contents.addProperty("type", entityType.getKey().toString());
            contents.addProperty("id", entityId.toString());
            object.add("contents", contents);
            return object;
        }

        @Override
        public ShowEntity duplicate() {
            return new ShowEntity(name, entityType, entityId);
        }
    }
}
