/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner;

import de.skuzzle.semantic.Version;
import java.lang.Exception;
import java.lang.String;
import java.text.SimpleDateFormat;
import lombok.Getter;

public final class BuildInfo {
  @Getter
  private final Version version = Version.parseVersion("3.0.2");

  @Getter
  private final String branch = "branch";

  @Getter
  private final boolean dirty = true;

  public String getTimestamp() {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2021-04-08T14:34:13.901-0600").toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
