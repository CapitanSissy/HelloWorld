package com.github.capitansissy.enumeration;

import org.jetbrains.annotations.Contract;

public enum Database {
  Initializer(0, ""),
  Data(1, ""),
  Log(2, "");

  private int code;
  private String description;

  Database(int code, String desc) {
    this.code = code;
    this.description = desc;
  }

  @Contract(pure = true)
  public static int getSize() {
    return Database.values().length;
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
