package com.github.capitansissy.security;

import com.github.capitansissy.security.implement.Linux;
import com.github.capitansissy.security.implement.MacOS;
import com.github.capitansissy.security.implement.Windows;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Nullable;

public class Hardware {
  @Nullable
  public static String getSerialNumber() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return Windows.getSerialNumber();
    }
    if (SystemUtils.IS_OS_LINUX) {
      return Linux.getSerialNumber();
    }
    if (SystemUtils.IS_OS_MAC_OSX) {
      return MacOS.getSerialNumber();
    }
    return null;
  }
}
