package com.github.capitansissy.database;

import java.io.Serializable;

public class Inquiries implements Serializable {
  public static final String GET_CURRENT_DATABASE = "SELECT DATABASE() FROM DUAL";
  public static final String INITIALIZE_DEFAULT_DATABASE = "SELECT COUNT(*) FROM DUAL";
  public static final String GET_TABLE_LIST = "SELECT `%1$s`.`%2$s`.`serverName`, `%1$s`.`%2$s`.`portNumber`, `%1$s`.`%2$s`.`databaseName`, `%1$s`.`%2$s`.`useSSL`, `%1$s`.`%2$s`.`username`, `%1$s`.`%2$s`.`password` FROM `%1$s`.`%2$s` WHERE `%1$s`.`%2$s`.`rowId` = ?";

}
