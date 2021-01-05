/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.textures;

import java.text.MessageFormat;

/*
 * Exception
 */
public class InvalidTextureException extends RuntimeException{
    public static final String EXCEPTION_MESSAGE = "The specified texture {0} was invalid";
    public InvalidTextureException(String textureString){
        super(MessageFormat.format(EXCEPTION_MESSAGE, textureString));
    }

    public InvalidTextureException(String textureString, Exception e){
        super(MessageFormat.format(EXCEPTION_MESSAGE, textureString), e);
    }
}
