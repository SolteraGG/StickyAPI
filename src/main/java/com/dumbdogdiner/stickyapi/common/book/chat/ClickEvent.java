/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;

/**
 * An event that is fired when text associated with this event is clicked.
 */
public abstract class ClickEvent {
    private ClickEvent() {} // seal this class

    /**
     * Convert the ClickEvent into JSON representation used in-game.
     * @return {@link JsonElement}
     */
    public abstract @NonNull JsonElement toJson();

    /**
     * Create a copy of this ClickEvent.
     * @return {@link ClickEvent}
     */
    public abstract @NonNull ClickEvent duplicate();

    /**
     * When clicked, displays a prompt to open a URL in the player's browser.
     */
    public final static class OpenUrl extends ClickEvent {
        /** The URL to open. */
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

    /**
     * When clicked, opens a file on the player's computer. This event can only be used by messages generated by the
     * game, and will not function otherwise.
     */
    public final static class OpenFile extends ClickEvent {
        /** The path to open. */
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

    /**
     * When clicked, executes the command as if the player said it in chat, either sending a message or executing a
     * command. This can also be used to execute commands on signs, but only on the root component, with the slash
     * omitted, and run by the server with @s set to the player.
     */
    public final static class RunCommand extends ClickEvent {
        /** The command or message to run. */
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

    /**
     * Behaves similarly to {@link RunCommand}, but does not actually execute the command, instead opening the chat box
     * and pasting the command there.
     */
    public final static class SuggestCommand extends ClickEvent {
        /** The command to suggest. */
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

    /**
     * When clicked, jumps to a given page. Obviously only works in books.
     */
    public final static class ChangePage extends ClickEvent {
        /** The page to go to. */
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

    /**
     * When clicked, text is copied to the clipboard.
     */
    public final static class CopyToClipboard extends ClickEvent {
        /** The text to copy. */
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
