package com.github.capitansissy.enumeration;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.security.AES;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.ResourceBundle;

public enum Boolean {
  False(0, ""),
  True(1, "");

  private int code;
  private String description;

  Boolean(int code, String desc) {
    this.code = code;
    this.description = desc;
  }

  @Contract(pure = true)
  public static int getSize() {
    return Boolean.values().length;
  }

  @NotNull
  public static String toCode(String string) {
    if (Objects.requireNonNull(AES.decrypt(ResourceBundle.getBundle("messages").getString("boolean.false"), Defaults.INTERNAL_SECURITY_KEY)).equals(string)) {
      return String.valueOf(False.getCode());
    }
    return String.valueOf(True.getCode());
  }

  public static String toString(String code) {
    if ("0".equals(code)) {
      return AES.decrypt(ResourceBundle.getBundle("messages").getString("boolean.false"), Defaults.INTERNAL_SECURITY_KEY);
    }
    return AES.decrypt(ResourceBundle.getBundle("messages").getString("boolean.true"), Defaults.INTERNAL_SECURITY_KEY);
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