/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.commonmarkextensions;

import lombok.Getter;
import org.commonmark.node.CustomNode;

public class MCColorNode extends CustomNode {
    @Getter
    private final String colorName;

    public MCColorNode(String colorName) {
        this.colorName = colorName;
    }
}
