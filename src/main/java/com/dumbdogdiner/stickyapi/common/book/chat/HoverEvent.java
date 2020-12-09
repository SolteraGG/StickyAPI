/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

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
        private final @NonNull String itemId;
        @Getter
        private final @Nullable Integer count;
        @Getter
        private final @Nullable String nbt;

        public ShowItem(@NonNull String itemId, @Nullable Integer count, @Nullable String nbt) {
            this.itemId = itemId;
            this.count = count;
            this.nbt = nbt;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "show_item");
            JsonObject contents = new JsonObject();
            if (itemId != null) {
                contents.addProperty("id", itemId);
            }
            if (count != null) {
                contents.addProperty("count", count);
            }
            if (nbt != null) {
                contents.addProperty("tag", nbt);
            }
            object.add("contents", contents);
            return object;
        }

        @Override
        public ShowItem duplicate() {
            return new ShowItem(itemId, count, nbt);
        }
    }

    public final static class ShowEntity extends HoverEvent {
        @Getter
        private final @Nullable JsonComponent name;
        @Getter
        private final @NonNull String entityKey;
        @Getter
        private final @NonNull UUID entityId;

        public ShowEntity(@Nullable JsonComponent name, @NonNull String entityKey, @NonNull UUID entityId) {
            this.name = name;
            this.entityKey = entityKey;
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
            contents.addProperty("type", entityKey);
            contents.addProperty("id", entityId.toString());
            object.add("contents", contents);
            return object;
        }

        @Override
        public ShowEntity duplicate() {
            return new ShowEntity(name, entityKey, entityId);
        }
    }
}
