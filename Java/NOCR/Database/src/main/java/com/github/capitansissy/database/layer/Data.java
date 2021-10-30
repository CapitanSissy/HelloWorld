package com.github.capitansissy.database.layer;

import com.github.capitansissy.database.abstracts.EntityBase;

import java.io.Serializable;
import java.sql.Connection;

class Data extends EntityBase implements Serializable {

  Data(Connection connection) {
    super(connection);
  }

}
