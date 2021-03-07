package com.dumbdogdiner.stickyapi;

import de.skuzzle.semantic.Version;
import java.lang.Exception;
import java.lang.String;
import java.text.SimpleDateFormat;
import lombok.Getter;

public final class BuildInfo {
  @Getter
    private final Version version = Version.parseVersion("2.2.0");

  @Getter
  private final String branch = "feat/aakatz3/buildinfo";

  @Getter
  private final boolean dirty = true;

  public String getTimestamp() {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2021-03-07T14:54:25.022-0700").toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
