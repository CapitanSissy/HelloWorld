package com.github.capitansissy.enumeration;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.security.AES;
import org.jetbrains.annotations.Contract;

import java.util.ResourceBundle;

public enum Language {
  Farsi(0, AES.decrypt(ResourceBundle.getBundle("messages").getString("language.farsi"), Defaults.INTERNAL_SECURITY_KEY)),
  Arabic(1, AES.decrypt(ResourceBundle.getBundle("messages").getString("language.arabic"), Defaults.INTERNAL_SECURITY_KEY));

  private int code;
  private String description;

  Language(int code, String desc) {
    this.code = code;
    this.description = desc;
  }

  @Contract(pure = true)
  public static int getSize() {
    return Language.values().length;
  }

  @Contract(pure = true)
  public int getCode() {
    return code;
  }

  @Contract(pure = true)
  public String getDescription() {
    return description;
  }
}
