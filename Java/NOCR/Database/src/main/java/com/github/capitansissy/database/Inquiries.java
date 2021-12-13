package com.github.capitansissy.database;

import java.io.Serializable;

public class Inquiries implements Serializable {
  public static final String GET_CURRENT_DATABASE = "SELECT DATABASE() FROM DUAL";
  public static final String INITIALIZE_DEFAULT_DATABASE = "SELECT COUNT(*) FROM DUAL";
  public static final String GET_TABLE_LIST_OBFUSCATED = "SELECT `%1$s`.`%2$s`.`%3$s`, `%1$s`.`%2$s`.`%4$s`, `%1$s`.`%2$s`.`%5$s`, `%1$s`.`%2$s`.`%6$s`, `%1$s`.`%2$s`.`%7$s`, `%1$s`.`%2$s`.`%8$s` FROM `%1$s`.`%2$s` WHERE `%1$s`.`%2$s`.`%9$s` = ?";

}
