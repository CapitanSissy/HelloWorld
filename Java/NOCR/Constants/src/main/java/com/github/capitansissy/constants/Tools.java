package com.github.capitansissy.constants;

import com.github.capitansissy.enumeration.OSType;

import java.io.Serializable;

import static com.github.capitansissy.constants.Defaults.OS_NAME;
import static com.github.capitansissy.enumeration.OSType.*;

public class Tools implements Serializable {
  public static OSType getOsType() {
    String osNameLower = OS_NAME.toLowerCase();
    if (osNameLower.startsWith("win")) {
      return WINDOWS;
    }
    if (osNameLower.startsWith("linux")) {
      return LINUX;
    }
    if (osNameLower.startsWith("mac")) {
      return MAC_OS;
    }
    throw new UnsupportedOperationException("Your Operative System (" + osNameLower + ") is not supported!");
  }

  public static String getOsVersion() {
    return System.getProperty("os.version");
  }

}
