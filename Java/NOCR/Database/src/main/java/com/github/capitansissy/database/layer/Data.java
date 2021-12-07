package com.github.capitansissy.database.layer;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.database.Inquiries;
import com.github.capitansissy.database.abstracts.EntityBase;
import com.github.capitansissy.encapsulation.ConnectionParameters;
import com.github.capitansissy.enumeration.Database;
import com.github.capitansissy.security.AES;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Data extends EntityBase implements Serializable {

  Data(Connection connection) {
    super(connection);
  }

  void InitializeDatabaseAsPrimitive() throws SQLException {
    PreparedStatement preparedStatement = makeConnection().prepareStatement(Inquiries.INITIALIZE_DEFAULT_DATABASE);
    preparedStatement.execute();
    preparedStatement.close();
  }

  void InitializeDatabaseAsSecondary() throws SQLException {
    PreparedStatement preparedStatement = makeConnection().prepareStatement(Inquiries.GET_CURRENT_DATABASE);
    if (Defaults.DEBUG_MODE) {
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        System.out.println("database result is: ".concat(resultSet.getString(1)));
      }
      resultSet.close();
    } else preparedStatement.execute();
    preparedStatement.close();
  }

  ConnectionParameters GetTableInfoAsData() throws SQLException {
    return GetBaseTableInfo(Database.Data);
  }

  ConnectionParameters GetTableInfoAsLog() throws SQLException {
    return GetBaseTableInfo(Database.Log);
  }

  @NotNull
  private ConnectionParameters GetBaseTableInfo(@NotNull Database database) throws SQLException {
    ConnectionParameters connectionParameters = new ConnectionParameters();
    PreparedStatement preparedStatement = makeConnection().prepareStatement(String.format(Inquiries.GET_TABLE_LIST, Defaults.DEFAULT_SCHEMA, Defaults.Tables.TBL_CONNECTION_PARAMETERS));
    preparedStatement.setInt(1, database.getCode());
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      connectionParameters.setServerName(AES.decrypt(resultSet.getString(1), Defaults.PUBLIC_DATABASE_KEY));
      connectionParameters.setPortNumber(AES.decrypt(resultSet.getString(2), Defaults.PUBLIC_DATABASE_KEY));
      connectionParameters.setDatabaseName(AES.decrypt(resultSet.getString(3), Defaults.PUBLIC_DATABASE_KEY));
      connectionParameters.setUseSSL(AES.decrypt(resultSet.getString(4), Defaults.PUBLIC_DATABASE_KEY));
      connectionParameters.setUsername(AES.decrypt(resultSet.getString(5), Defaults.PUBLIC_DATABASE_KEY));
      connectionParameters.setPassword(AES.decrypt(resultSet.getString(6), Defaults.PUBLIC_DATABASE_KEY));
    }
    resultSet.close();
    preparedStatement.close();
    return connectionParameters;
  }

}
