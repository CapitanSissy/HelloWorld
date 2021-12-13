package com.github.capitansissy.enumeration;

import java.util.Arrays;

public enum ConnectionParameters {
  ROW_ID(1, "TBL_CP_C"),
  SERVER_NAME(2, "TBL_CP_CC"),
  PORT_NUMBER(3, "TBL_CP_CCC"),
  DATABASE_NAME(4, "TBL_CP_CCCC"),
  USE_SSL(5, "TBL_CP_CCCCC"),
  USERNAME(6, "TBL_CP_CCCCCC"),
  PASSWORD(7, "TBL_CP_CCCCCCC");

  private final int id;
  private final String obfuscatedName;

  ConnectionParameters(int id, String obfuscatedName) {
    this.id = id;
    this.obfuscatedName = obfuscatedName;
  }

//  public static String[] getColumnsName(Class<? extends Enum<?>> e) {
//    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
//  }

  public static String[] getColumnsName() {
    return Arrays.stream(values()).map(ConnectionParameters::getObfuscatedName).toArray(String[]::new);
    // return Stream.of(values()).map(Parameters::getObfuscatedName).toArray(String[]::new);
  }

  private int getId() {
    return id;
  }

  public String getObfuscatedName() {
    return obfuscatedName;
  }
}
