/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A text component that can be converted to JSON. Differs slightly from BaseComponent.
 */
public class JsonComponent {
    @Getter @Setter
    private @NonNull String text = "";
    @Getter @Setter
    private @Nullable Boolean bold = null;
    @Getter @Setter
    private @Nullable Boolean italic = null;
    @Getter @Setter
    private @Nullable Boolean underlined = null;
    @Getter @Setter
    private @Nullable Boolean strikethrough = null;
    @Getter @Setter
    private @Nullable Boolean obfuscated = null;
    @Getter @Setter
    private @Nullable ComponentColor color = null;
    @Getter @Setter
    private @Nullable ClickEvent clickEvent = null;
    @Getter @Setter
    private @Nullable HoverEvent hoverEvent = null;
    @Getter
    private final List<JsonComponent> children = new ArrayList<>();

    public JsonComponent() {}

    public JsonComponent(@NonNull String text) {
        this.text = text;
    }

    /**
     * Converts the component into JSON.
     * @return {@link com.google.gson.JsonObject}
     */
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("text", text);
        if (bold != null) object.addProperty("bold", bold);
        if (italic != null) object.addProperty("italic", italic);
        if (underlined != null) object.addProperty("underlined", underlined);
        if (strikethrough != null) object.addProperty("strikethrough", strikethrough);
        if (obfuscated != null) object.addProperty("obfuscated", obfuscated);
        if (color != null) {
            object.addProperty("color", color.getText());
        }
        if (clickEvent != null) {
            object.add("clickEvent", clickEvent.toJson());
        }
        if (hoverEvent != null) {
            object.add("hoverEvent", hoverEvent.toJson());
        }
        if (!children.isEmpty()) {
            JsonArray extra = new JsonArray();
            for (JsonComponent component : children) {
                extra.add(component.toJson());
            }
            object.add("extra", extra);
        }
        return object;
    }

    /**
     * Flatten a JsonComponent into an array of JsonComponents with no children.
     * @return {@link java.util.List}
     */
    public List<JsonComponent> flatten() {
        return flattenWithParents(new ArrayList<>());
    }

    private List<JsonComponent> flattenWithParents(List<JsonComponent> parents) {
        List<JsonComponent> components = new ArrayList<>();
        if (!text.equals("")) {
            JsonComponent flatComponent = new JsonComponent(text);
            flatComponent.setBold(orFromList(bold, parents, JsonComponent::getBold));
            flatComponent.setItalic(orFromList(italic, parents, JsonComponent::getItalic));
            flatComponent.setUnderlined(orFromList(underlined, parents, JsonComponent::getUnderlined));
            flatComponent.setStrikethrough(orFromList(strikethrough, parents, JsonComponent::getStrikethrough));
            flatComponent.setObfuscated(orFromList(obfuscated, parents, JsonComponent::getObfuscated));
            flatComponent.setColor(orFromList(color, parents, JsonComponent::getColor));
            flatComponent.setClickEvent(orFromList(clickEvent, parents, JsonComponent::getClickEvent));
            flatComponent.setHoverEvent(orFromList(hoverEvent, parents, JsonComponent::getHoverEvent));
            components.add(flatComponent);
        }
        List<JsonComponent> newParents = Stream.concat(Stream.of(this), parents.stream()).collect(Collectors.toList());
        for (JsonComponent child : children) {
            components.addAll(child.flattenWithParents(newParents));
        }
        return components;
    }

    private <T> T orFromList(T t, List<JsonComponent> components, Function<JsonComponent, T> getter) {
        if (t == null) {
            return components.stream().map(getter).filter(Objects::nonNull).findFirst().orElse(null);
        } else {
            return t;
        }
    }

    /**
     * Clone this component
     * @return A clone of this component
     */
    public JsonComponent duplicate() {
        JsonComponent clone = new JsonComponent(text);
        clone.bold = bold;
        clone.italic = italic;
        clone.underlined = underlined;
        clone.strikethrough = strikethrough;
        clone.obfuscated = obfuscated;
        clone.color = color;
        if (clickEvent != null) clone.clickEvent = clickEvent.duplicate();
        if (hoverEvent != null) clone.hoverEvent = hoverEvent.duplicate();
        for (JsonComponent child : children) {
            clone.children.add(child.duplicate());
        }
        return clone;
    }

}
