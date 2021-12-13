package com.github.capitansissy.database;

import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.database.layer.Business;
import com.github.capitansissy.encapsulation.Parameters;
import com.github.capitansissy.enumeration.Database;
import com.github.capitansissy.messages.ResourceAsStream;
import com.github.capitansissy.security.AES;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Objects;

public class DataAccess implements Serializable {
  private static Logger logger = new Logger();
  private static ComboPooledDataSource initializerComboPooled = null;
  private static ComboPooledDataSource dataComboPooled = null;
  private static ComboPooledDataSource logComboPooled = null;
  private static ResourceAsStream resource = new ResourceAsStream();

  @NotNull
  private static ComboPooledDataSource getBaseDataSourceConfiguration(@NotNull ComboPooledDataSource comboPooledDataSource) throws PropertyVetoException {
    comboPooledDataSource.setDriverClass(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.driverClass"), Defaults.INTERNAL_SECURITY_KEY));
    comboPooledDataSource.setMinPoolSize(Integer.parseInt(Objects.requireNonNull(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.minPoolSize"), Defaults.INTERNAL_SECURITY_KEY))));
    comboPooledDataSource.setMaxPoolSize(Integer.parseInt(Objects.requireNonNull(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.maxPoolSize"), Defaults.INTERNAL_SECURITY_KEY))));
    comboPooledDataSource.setAcquireIncrement(Integer.parseInt(Objects.requireNonNull(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.acquireIncrement"), Defaults.INTERNAL_SECURITY_KEY))));
    comboPooledDataSource.setTestConnectionOnCheckin(Boolean.parseBoolean(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.testConnectionOnCheckin"), Defaults.INTERNAL_SECURITY_KEY)));
    comboPooledDataSource.setTestConnectionOnCheckout(Boolean.parseBoolean(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.testConnectionOnCheckout"), Defaults.INTERNAL_SECURITY_KEY)));
    comboPooledDataSource.setIdleConnectionTestPeriod(Integer.parseInt(Objects.requireNonNull(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.idleConnectionTestPeriod"), Defaults.INTERNAL_SECURITY_KEY))));
    comboPooledDataSource.setMaxConnectionAge(Integer.parseInt(Objects.requireNonNull(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.maxConnectionAge"), Defaults.INTERNAL_SECURITY_KEY))));
    comboPooledDataSource.setMaxIdleTimeExcessConnections(Integer.parseInt(Objects.requireNonNull(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.maxIdleTimeExcessConnections"), Defaults.INTERNAL_SECURITY_KEY))));
    comboPooledDataSource.setConnectionCustomizerClassName(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.connectionCustomizerClassName"), Defaults.INTERNAL_SECURITY_KEY));
    return comboPooledDataSource;
  }

  @NotNull
  private static ComboPooledDataSource AsInitializer() throws PropertyVetoException {
    initializerComboPooled = new ComboPooledDataSource();
    initializerComboPooled.setJdbcUrl(String.format(Defaults.C3P0_DATASOURCE_JDBCURL,
      AES.decrypt(Tools.getResourceValue("db", "project.database.default.server.name"), Defaults.INTERNAL_SECURITY_KEY),
      AES.decrypt(Tools.getResourceValue("db", "project.database.default.port.number"), Defaults.INTERNAL_SECURITY_KEY),
      AES.decrypt(Tools.getResourceValue("db", "project.database.default.name"), Defaults.INTERNAL_SECURITY_KEY),
      AES.decrypt(Tools.getResourceValue("db", "project.database.default.use.ssl"), Defaults.INTERNAL_SECURITY_KEY)));
    initializerComboPooled.setUser(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.user"), Defaults.INTERNAL_SECURITY_KEY));
    initializerComboPooled.setPassword(AES.decrypt(Tools.getResourceValue("db", "c3p0.datasource.password"), Defaults.INTERNAL_SECURITY_KEY));
    return getBaseDataSourceConfiguration(initializerComboPooled);
  }

  @NotNull
  private static ComboPooledDataSource AsData(@NotNull Parameters parameters) throws PropertyVetoException {
    dataComboPooled = new ComboPooledDataSource();
    dataComboPooled.setJdbcUrl(String.format(Defaults.C3P0_DATASOURCE_JDBCURL,
      parameters.getServerName(),
      parameters.getPortNumber(),
      parameters.getDatabaseName(),
      parameters.getUseSSL()));
    dataComboPooled.setUser(parameters.getUsername());
    dataComboPooled.setPassword(parameters.getPassword());
    return getBaseDataSourceConfiguration(dataComboPooled);
  }

  @NotNull
  private static ComboPooledDataSource AsLog(@NotNull Parameters parameters) throws PropertyVetoException {
    logComboPooled = new ComboPooledDataSource();
    logComboPooled.setJdbcUrl(String.format(Defaults.C3P0_DATASOURCE_JDBCURL,
      parameters.getServerName(),
      parameters.getPortNumber(),
      parameters.getDatabaseName(),
      parameters.getUseSSL()));
    logComboPooled.setUser(parameters.getUsername());
    logComboPooled.setPassword(parameters.getPassword());
    return getBaseDataSourceConfiguration(logComboPooled);
  }

  public static Connection getConnection() throws Exception {
    return getConnection(Database.Initializer.getCode());
  }

  public static Connection getConnection(int databaseType) throws Exception {
    Connection connection;
    switch (databaseType) {
      /**
       * Initialize and make connection pool as data
       *
       * @return connection of dataComboPooled, or throw new exception as database not available.
       */
      case 1:
        try {
          if (dataComboPooled == null) {
            Parameters parameters = new Business().GetTableInfoAsData();
            dataComboPooled = AsData(parameters);
          }
          connection = dataComboPooled.getConnection();
        } catch (Exception exception) {
          logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
          throw new Exception(AES.decrypt(resource.get("db.not.available"), Defaults.INTERNAL_SECURITY_KEY));
        }
        break;
      /**
       * Initialize and make connection pool as log
       *
       * @return connection of logComboPooled, or throw new exception as database not available.
       */
      case 2:
        try {
          if (logComboPooled == null) {
            Parameters parameters = new Business().GetTableInfoAsLog();
            logComboPooled = AsLog(parameters);
          }
          connection = logComboPooled.getConnection();
        } catch (Exception exception) {
          logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
          throw new Exception(AES.decrypt(resource.get("db.not.available"), Defaults.INTERNAL_SECURITY_KEY));
        }
        break;
      /**
       * Initialize and make connection pool as core database
       *
       * @return connection of initializerComboPooled, or throw new exception as database not available.
       */
      default:
        try {
          if (initializerComboPooled == null)
            initializerComboPooled = AsInitializer();
          connection = initializerComboPooled.getConnection();
        } catch (Exception exception) {
          logger.setLog(exception.getMessage(), Defaults.Log4J.FATAL);
          throw new Exception(AES.decrypt(resource.get("db.not.available"), Defaults.INTERNAL_SECURITY_KEY));
        }
        break;
    }
    return connection;
  }

  public static void dispose(Connection connection) throws Exception {
    if ((connection != null) && (!connection.isClosed())) {
      connection.close();
    }
    connection = null;
  }


}
