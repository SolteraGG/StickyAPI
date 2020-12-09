/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import com.dumbdogdiner.stickyapi.common.book.chat.ComponentColor;
import lombok.Getter;
import org.commonmark.node.CustomNode;

/**
 * A custom node that represents a {@link ComponentColor}.
 */
public class MCColorNode extends CustomNode {
    @Getter
    private final ComponentColor color;

    public MCColorNode(ComponentColor color) {
        this.color = color;
    }
}
