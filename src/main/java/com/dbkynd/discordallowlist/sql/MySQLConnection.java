package com.dbkynd.discordallowlist.sql;

import com.dbkynd.discordallowlist.DiscordAllowList;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnection {
  private static final Logger LOGGER = DiscordAllowList.LOGGER;

  private Connection connection;
  private String host;
  private String port;
  private String user;
  private String password;
  private String database;

  public MySQLConnection(String sqlHost, String sqlPort, String sqlDatabase, String sqlUser, String sqlPassword) {
    this.host = sqlHost;
    this.port = sqlPort;
    this.database = sqlDatabase;
    this.user = sqlUser;
    this.password = sqlPassword;
  }

  private Connection getConnection() {
    return connection;
  }

  public void connect() {
    if (host == null || host.equalsIgnoreCase("")) {
      LOGGER.error("You have not specified a host in the Main config!");
    } else if (user == null || user.equalsIgnoreCase("")) {
      LOGGER.error("You have not specified a user in the Main config!");
    } else if (password == null || password.equalsIgnoreCase("")) {
      LOGGER.error("You have not specified a password in the Main config!");
    } else if (database == null || database.equalsIgnoreCase("")) {
      LOGGER.error("You have not specified a database in the Main config!");
    } else {
      login();
    }
  }

  private void disconnect() {
    try {
      if (getConnection() != null) {
        connection.close();
      } else {
        LOGGER.error("There was an issue with MySQL: Main is not currently connected to a database.");
      }
    } catch (Exception e) {
      LOGGER.error("There was an issue with MySQL: " + e.getMessage());
    }
    connection = null;
  }

  private void reconnect() {
    disconnect();
    connect();
  }

  private void login() {
    try {
      if (getConnection() != null) {
        connection.close();
      }
    } catch (Exception e) {
      // Do Nothing
    }
    connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    } catch (Exception e) {
      LOGGER.error("There was an issue with MySQL: " + e.getMessage());
    }
  }

  private ResultSet query(String query) {
    if (query == null) {
      return null;
    }
    connect();
    ResultSet results = null;
    try {
      Statement statement = getConnection().createStatement();
      results = statement.executeQuery(query);
    } catch (Exception e) {
      LOGGER.error("There has been an error:" + e.getMessage());
      LOGGER.error("Failed Query in MySQL using the following query input:");
      LOGGER.error(query);
    }
    return results;
  }

  public void update(String input) {
    if (input == null) {
      return;
    }
    connect();
    try {
      Statement statement = getConnection().createStatement();
      statement.executeUpdate(input);
      statement.close();
    } catch (Exception e) {
      LOGGER.error("There has been an error:" + e.getMessage());
      LOGGER.error("Failed to update MySQL using the following update input:");
      LOGGER.error(input);
    }
  }

  public boolean tableExists(String tableName) {
    if (tableName == null) {
      return false;
    }
    try {
      if (getConnection() == null) {
        return false;
      }
      if (getConnection().getMetaData() == null) {
        return false;
      }
      ResultSet results = getConnection().getMetaData().getTables(null, null, tableName, null);
      if (results.next()) {
        return true;
      }
    } catch (Exception localException) {
      // Do Nothing
    }
    return false;
  }

  public boolean itemExists(String column, String data, String table) {
    if (data != null) {
      data = "'" + data + "'";
    }
    try {
      ResultSet results = query("SELECT * FROM " + table + " WHERE " + column + "=" + data);
      while (results.next()) {
        if (results.getString(column) != null) {
          return true;
        }
      }
    } catch (Exception localException) {
      // Do Nothing
    }
    return false;
  }

  public void set(String selected, Object object, String column, String equality, String data, String table) {
    if (object != null) {
      object = "'" + object + "'";
    }
    if (data != null) {
      data = "'" + data + "'";
    }
    update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + equality + data + ";");
  }

  public Object get(String selected, String column, String equality, String data, String table) {
    if (data != null) {
      data = "'" + data + "'";
    }
    try {
      ResultSet rs = query("SELECT * FROM " + table + " WHERE " + column + equality + data);
      if (rs.next()) {
        return rs.getObject(selected);
      }
    } catch (Exception localException) {
      // Do Nothing
    }
    return null;
  }
}
