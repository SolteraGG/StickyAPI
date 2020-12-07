/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;

public abstract class ClickEvent {
    private ClickEvent() { }

    public abstract JsonElement toJson();

    public abstract ClickEvent duplicate();

    public final static class OpenUrl extends ClickEvent {
        @Getter
        private final @NonNull String url;

        public OpenUrl(@NonNull String url) {
            this.url = url;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "open_url");
            object.addProperty("value", url);
            return object;
        }

        @Override
        public OpenUrl duplicate() {
            return new OpenUrl(url);
        }
    }

    // shouldn't work but included for completeness
    public final static class OpenFile extends ClickEvent {
        @Getter
        private final @NonNull String path;

        public OpenFile(@NonNull String path) {
            this.path = path;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "open_file");
            object.addProperty("value", path);
            return object;
        }

        @Override
        public OpenFile duplicate() {
            return new OpenFile(path);
        }
    }

    public final static class RunCommand extends ClickEvent {
        @Getter
        private final @NonNull String command;

        public RunCommand(@NonNull String command) {
            this.command = command;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "run_command");
            object.addProperty("value", command);
            return object;
        }

        @Override
        public RunCommand duplicate() {
            return new RunCommand(command);
        }
    }

    public final static class SuggestCommand extends ClickEvent {
        @Getter
        private final @NonNull String command;

        public SuggestCommand(@NonNull String command) {
            this.command = command;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "suggest_command");
            object.addProperty("value", command);
            return object;
        }

        @Override
        public SuggestCommand duplicate() {
            return new SuggestCommand(command);
        }
    }

    public final static class ChangePage extends ClickEvent {
        @Getter
        private final int page;

        public ChangePage(int page) {
            this.page = page;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "change_page");
            object.addProperty("value", page);
            return object;
        }

        @Override
        public ChangePage duplicate() {
            return new ChangePage(page);
        }
    }

    public final static class CopyToClipboard extends ClickEvent {
        @Getter
        private final @NonNull String text;

        public CopyToClipboard(@NonNull String text) {
            this.text = text;
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("action", "copy_to_clipboard");
            object.addProperty("value", text);
            return object;
        }

        @Override
        public CopyToClipboard duplicate() {
            return new CopyToClipboard(text);
        }
    }
}
