/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
    // for now, the attributes are only what are needed for the renderer, might expand later
    @Getter @Setter
    private boolean isBold = false;
    @Getter @Setter
    private boolean isItalic = false;
    @Getter @Setter
    private boolean isUnderlined = false;
    @Getter @Setter
    private String color = null;
    @Getter @Setter
    private String url = null;
    @Getter @Setter
    private String hoverText = null;
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
        if (isBold) object.addProperty("bold", true);
        if (isItalic) object.addProperty("italic", true);
        if (isUnderlined) object.addProperty("underline", true);
        if (color != null) {
            object.addProperty("color", color);
        }
        if (url != null) {
            JsonObject clickEvent = new JsonObject();
            clickEvent.addProperty("action", "open_url");
            clickEvent.addProperty("value", url);
            object.add("clickEvent", clickEvent);
        }
        if (hoverText != null) {
            JsonObject hoverEvent = new JsonObject();
            hoverEvent.addProperty("action", "show_text");
            hoverEvent.addProperty("contents", hoverText);
            object.add("hoverEvent", hoverEvent);
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
            flatComponent.setBold(isBold || parents.stream().anyMatch(c -> c.isBold));
            flatComponent.setItalic(isItalic || parents.stream().anyMatch(c -> c.isItalic));
            flatComponent.setUnderlined(isUnderlined || parents.stream().anyMatch(c -> c.isUnderlined));
            if (color == null) {
                flatComponent.setColor(findFromList(parents, JsonComponent::getColor));
            } else {
                flatComponent.setColor(color);
            }
            if (url == null) {
                flatComponent.setUrl(findFromList(parents, JsonComponent::getUrl));
            } else {
                flatComponent.setUrl(url);
            }
            if (hoverText == null) {
                flatComponent.setHoverText(findFromList(parents, JsonComponent::getHoverText));
            } else {
                flatComponent.setHoverText(hoverText);
            }
            components.add(flatComponent);
        }
        List<JsonComponent> newParents = Stream.concat(Stream.of(this), parents.stream()).collect(Collectors.toList());
        for (JsonComponent child : children) {
            components.addAll(child.flattenWithParents(newParents));
        }
        return components;
    }

    private <T> T findFromList(List<JsonComponent> components, Function<JsonComponent, T> getter) {
        return components.stream().map(getter).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public JsonComponent duplicate() {
        JsonComponent clone = new JsonComponent(text);
        clone.isBold = isBold;
        clone.isItalic = isItalic;
        clone.isUnderlined = isUnderlined;
        clone.color = color;
        clone.url = url;
        clone.hoverText = hoverText;
        for (JsonComponent child : children) {
            clone.children.add(child.duplicate());
        }
        return clone;
    }
}
