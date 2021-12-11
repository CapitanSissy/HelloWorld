package com.github.capitansissy.pre.compile;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.encapsulation.ConnectionParameters;
import com.github.capitansissy.enumeration.Database;
import com.github.capitansissy.security.AES;

import java.io.IOException;

public class EncryptParams {

  private static void encryptDatabaseParameters(ConnectionParameters connectionParameters, int databaseType) throws IOException {
    final String INSERT_QUERY = "├→ INSERT INTO `%1$s`.`%2$s`(`rowId`, `serverName`, `portNumber`, `databaseName`, `useSSL`, `username`, `password`) VALUES (%9$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s');";
    final String UPDATE_QUERY = "├→ UPDATE `%1$s`.`%2$s` SET `serverName` = '%3$s', `portNumber` = '%4$s', `databaseName` = '%5$s', `useSSL` = '%6$s', `username` = '%7$s', `password` = '%8$s' WHERE `rowId` = %9$s;";
    switch (databaseType) {
      case 1:
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println("Choose the right information according to your needs".concat(Defaults.Slugs.Ellipsis));
        System.out.println(String.format(INSERT_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Defaults.Tables.TBL_CONNECTION_PARAMETERS,
          AES.encrypt(connectionParameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Data.getCode()));
        System.out.println(String.format(UPDATE_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Defaults.Tables.TBL_CONNECTION_PARAMETERS,
          AES.encrypt(connectionParameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Data.getCode()));
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println(Defaults.Slugs.CRLF);
        break;
      case 2:
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println("Choose the right information according to your needs".concat(Defaults.Slugs.Ellipsis));
        System.out.println(String.format(INSERT_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Defaults.Tables.TBL_CONNECTION_PARAMETERS,
          AES.encrypt(connectionParameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Log.getCode()));
        System.out.println(String.format(UPDATE_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Defaults.Tables.TBL_CONNECTION_PARAMETERS,
          AES.encrypt(connectionParameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(connectionParameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Log.getCode()));
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println(Defaults.Slugs.CRLF);
        break;
      default:
        System.out.println("Wrong usage");
        break;
    }
  }

  public static void main(String[] args) {
    try {
      /*=-=-=-=-=-[ Database section ]-=-=-=-=-=*/
      ConnectionParameters log = new ConnectionParameters();
      log.setServerName("localhost");
      log.setPortNumber("3306");
      log.setDatabaseName(Defaults.LOG_SCHEMA);
      log.setUseSSL("0");
      log.setUsername("helloworld_log");
      log.setPassword("qazQAZ@789");
      encryptDatabaseParameters(log, Database.Log.getCode());

      ConnectionParameters data = new ConnectionParameters();
      data.setServerName("localhost");
      data.setPortNumber("3306");
      data.setDatabaseName(Defaults.DATA_SCHEMA);
      data.setUseSSL("0");
      data.setUsername("helloworld_data");
      data.setPassword("qazQAZ@456");
      encryptDatabaseParameters(data, Database.Data.getCode());
    } catch(Exception e) {
      e.printStackTrace();
    }

  }
}
