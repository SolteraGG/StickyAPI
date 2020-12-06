/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import lombok.Getter;
import org.commonmark.node.CustomNode;

public class MCColorNode extends CustomNode {
    @Getter
    private final String colorName;

    public MCColorNode(String colorName) {
        this.colorName = colorName;
    }
}
