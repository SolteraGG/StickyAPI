/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

import java.util.UUID;

public abstract class HoverEvent {
    private HoverEvent() {}

    public abstract JsonElement toJson();

    public abstract HoverEvent duplicate();

    public final static class ShowText extends HoverEvent {
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

    public final static class ShowItem extends HoverEvent {
        @Getter
        private final @NonNull ItemStack stack;

        public ShowItem(@NonNull ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "show_item");
            JsonObject contents = new JsonObject();
            contents.addProperty("id", IRegistry.ITEM.getKey(stack.getItem()).toString());
            contents.addProperty("count", stack.getCount());
            NBTTagCompound tag = stack.getTag();
            if (tag != null) {
                contents.addProperty("tag", tag.toString());
            }
            object.add("contents", contents);
            return object;
        }

        @Override
        public ShowItem duplicate() {
            return new ShowItem(stack);
        }
    }

    public final static class ShowEntity extends HoverEvent {
        @Getter
        private final JsonComponent name;
        @Getter
        private final @NonNull EntityTypes<?> entityType;
        @Getter
        private final @NonNull UUID entityId;

        public ShowEntity(JsonComponent name, @NonNull EntityTypes<?> entityType, @NonNull UUID entityId) {
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
            contents.addProperty("type", IRegistry.ENTITY_TYPE.getKey(entityType).toString());
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
