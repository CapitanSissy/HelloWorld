package com.github.capitansissy.database.layer;

import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.database.DataAccess;
import com.github.capitansissy.encapsulation.Parameters;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Objects;

public class Business implements Serializable {
  private static Logger logger = new Logger();

  public void InitializeDatabaseAsPrimitive() throws Exception {
    try (Connection connection = DataAccess.getConnection()) {
      try {
        new Data(connection).InitializeDatabaseAsPrimitive();
      } catch (Exception exception) {
        Objects.requireNonNull(connection).rollback();
        logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
        throw new Exception(exception.getMessage());
      }
    }
  }

  public void InitializeDatabaseAsSecondary(int databaseType) throws Exception {
    if (Defaults.DEBUG_MODE)
      System.out.print(String.format("databaseType is: %1$s and ", String.valueOf(databaseType)));

    try (Connection connection = DataAccess.getConnection(databaseType)) {
      try {
        new Data(connection).InitializeDatabaseAsSecondary();
      } catch (Exception exception) {
        Objects.requireNonNull(connection).rollback();
        logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
        throw new Exception(exception.getMessage());
      }
    }
  }

  public Parameters GetTableInfoAsData() throws Exception {
    try (Connection connection = DataAccess.getConnection()) {
      try {
        return new Data(connection).GetTableInfoAsData();
      } catch (Exception exception) {
        Objects.requireNonNull(connection).rollback();
        logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
        throw new Exception(exception.getMessage());
      }
    }
  }

  public Parameters GetTableInfoAsLog() throws Exception {
    try (Connection connection = DataAccess.getConnection()) {
      try {
        return new Data(connection).GetTableInfoAsLog();
      } catch (Exception exception) {
        Objects.requireNonNull(connection).rollback();
        logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
        throw new Exception(exception.getMessage());
      }
    }
  }

}
