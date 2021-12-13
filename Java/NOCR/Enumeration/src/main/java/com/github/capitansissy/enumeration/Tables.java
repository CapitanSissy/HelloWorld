package com.github.capitansissy.enumeration;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.security.AES;

public enum Tables {
  TBL_CONNECTION_PARAMETERS(
    Defaults.DEFAULT_SCHEMA,
    AES.decrypt(Tools.getResourceValue("db", "project.database.default.name"), Defaults.INTERNAL_SECURITY_KEY),
    "TBL_CP",
    ConnectionParameters.getColumnsName()
  ),

  TBL_EVENT_LOG("",
    "",
    "", new String[]{});

  private String schema;
  private String database;
  private String table;
  private String[] columns;

  Tables(String schema, String database, String table, String[] columns) {
    this.schema = schema;
    this.database = database;
    this.table = table;
    this.columns = columns;
  }

  public String getSchema() {
    return schema;
  }

  public String getDatabase() {
    return database;
  }

  public String getTable() {
    return table;
  }

  public String[] getColumns() {
    return columns;
  }


}
