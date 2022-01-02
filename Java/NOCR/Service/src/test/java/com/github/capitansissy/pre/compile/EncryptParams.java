package com.github.capitansissy.pre.compile;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.encapsulation.Parameters;
import com.github.capitansissy.enumeration.ConnectionParameters;
import com.github.capitansissy.enumeration.Database;
import com.github.capitansissy.enumeration.Tables;
import com.github.capitansissy.security.AES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptParams {

  private static void encryptDatabaseParameters(Parameters parameters, int databaseType) throws IOException {
    final String INSERT_QUERY = "├→ INSERT INTO `%1$s`.`%2$s`(`%10$s`, `%11$s`, `%12$s`, `%13$s`, `%14$s`, `%15$s`, `%16$s`) VALUES (%9$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s');";
    final String UPDATE_QUERY = "├→ UPDATE `%1$s`.`%2$s` SET `serverName` = '%3$s', `portNumber` = '%4$s', `databaseName` = '%5$s', `useSSL` = '%6$s', `username` = '%7$s', `password` = '%8$s' WHERE `rowId` = %9$s;";
    switch (databaseType) {
      case 1:
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println("Choose the right information according to your needs".concat(Defaults.Slugs.Ellipsis));
        System.out.println(String.format(INSERT_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Tables.TBL_CONNECTION_PARAMETERS.getTable(),
          AES.encrypt(parameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Data.getCode(),
          ConnectionParameters.ROW_ID.getObfuscatedName(),
          ConnectionParameters.SERVER_NAME.getObfuscatedName(),
          ConnectionParameters.PORT_NUMBER.getObfuscatedName(),
          ConnectionParameters.DATABASE_NAME.getObfuscatedName(),
          ConnectionParameters.USE_SSL.getObfuscatedName(),
          ConnectionParameters.USERNAME.getObfuscatedName(),
          ConnectionParameters.PASSWORD.getObfuscatedName()));
        System.out.println(String.format(UPDATE_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Tables.TBL_CONNECTION_PARAMETERS.getTable(),
          AES.encrypt(parameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Data.getCode()));
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println(Defaults.Slugs.CRLF);
        break;
      case 2:
        System.out.println("█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█-█");
        System.out.println("Choose the right information according to your needs".concat(Defaults.Slugs.Ellipsis));
        System.out.println(String.format(INSERT_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Tables.TBL_CONNECTION_PARAMETERS.getTable(),
          AES.encrypt(parameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
          Database.Log.getCode(),
          ConnectionParameters.ROW_ID.getObfuscatedName(),
          ConnectionParameters.SERVER_NAME.getObfuscatedName(),
          ConnectionParameters.PORT_NUMBER.getObfuscatedName(),
          ConnectionParameters.DATABASE_NAME.getObfuscatedName(),
          ConnectionParameters.USE_SSL.getObfuscatedName(),
          ConnectionParameters.USERNAME.getObfuscatedName(),
          ConnectionParameters.PASSWORD.getObfuscatedName()));
        System.out.println(String.format(UPDATE_QUERY,
          Defaults.DEFAULT_SCHEMA,
          Tables.TBL_CONNECTION_PARAMETERS.getTable(),
          AES.encrypt(parameters.getServerName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPortNumber(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getDatabaseName(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUseSSL(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getUsername(), Defaults.PUBLIC_DATABASE_KEY),
          AES.encrypt(parameters.getPassword(), Defaults.PUBLIC_DATABASE_KEY),
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
      Parameters log = new Parameters();
      log.setServerName("localhost");
      log.setPortNumber("3306");
      log.setDatabaseName(Defaults.LOG_SCHEMA);
      log.setUseSSL("0");
      log.setUsername("helloworld_log");
      log.setPassword("qazQAZ@789");
      encryptDatabaseParameters(log, Database.Log.getCode());

      Parameters data = new Parameters();
      data.setServerName("localhost");
      data.setPortNumber("3306");
      data.setDatabaseName(Defaults.DATA_SCHEMA);
      data.setUseSSL("0");
      data.setUsername("helloworld_data");
      data.setPassword("qazQAZ@456");
      encryptDatabaseParameters(data, Database.Data.getCode());

      ImageCrypto.encryptImage(new FileInputStream(new File("D:/male.jpg")),
        new FileOutputStream(new File("D:/encrypted-male.jpg")));

      ImageCrypto.decryptImage(new FileInputStream("D:/encrypted-male.jpg"),
        new FileOutputStream(new File("D:/normal-male.jpg")));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
