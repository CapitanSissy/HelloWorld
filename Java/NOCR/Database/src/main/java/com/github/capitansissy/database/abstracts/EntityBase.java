package com.github.capitansissy.database.abstracts;

import java.io.Serializable;
import java.sql.Connection;

public class EntityBase implements Serializable {
  private Connection connection = null;

  public EntityBase(Connection connection) {
    this.connection = connection;
  }

  protected Connection makeConnection() {
    return connection;
  }

}
