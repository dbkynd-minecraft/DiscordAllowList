package com.dbkynd.discordallowlist.sql;

import com.dbkynd.discordallowlist.DiscordAllowList;
import com.dbkynd.discordallowlist.config.MySQLConfig;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class MySQLConnection {
    private static final Logger LOGGER = DiscordAllowList.LOGGER;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public MySQLConnection() throws Exception {
        host = MySQLConfig.host.get();
        port = MySQLConfig.port.get();
        database = MySQLConfig.database.get();
        String table = MySQLConfig.table.get();
        username = MySQLConfig.username.get();
        password = MySQLConfig.password.get();

        connect();
        // Create table if does not exist
        if (!tableExists(table)) {
            LOGGER.info(table + " table not found. Creating new table...");
            update("CREATE TABLE " + table + " (DiscordId CHAR(18), MinecraftName VARCHAR(16), UUID CHAR(36), PRIMARY KEY (DiscordID));");
            // Ensure table was created before saying so
            if (tableExists(table)) {
                LOGGER.info(table + " table created");
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void connect() throws Exception {
        try {
            if (getConnection() != null) {
                connection.close();
            }
        } catch (Exception e) {
            // Do Nothing
        }
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (Exception e) {
            throw new Exception("There was an issue connecting to MySql: " + e.getMessage());
        }
    }

    public ResultSet query(String query) {
        if (query == null) {
            return null;
        }
        ResultSet results = null;
        try {
            connect();
            Statement statement = getConnection().createStatement();
            results = statement.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("There has been an error:" + e.getMessage());
            LOGGER.error("Failed Query in MySql using the following query input:");
            LOGGER.error(query);
        }
        return results;
    }

    public void update(String input) {
        if (input == null) {
            return;
        }
        try {
            connect();
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(input);
            statement.close();
        } catch (Exception e) {
            LOGGER.error("There has been an error:" + e.getMessage());
            LOGGER.error("Failed to update MySql using the following update input:");
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

    public void set(String selected, Object object, String column, String equality, String data, String table) throws Exception {
        if (object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + equality + data + ";");
    }

    public UserRecord getByUuid(UUID uuid) {
        ResultSet rs;

        String table = MySQLConfig.table.get();

        if (itemExists("UUID", uuid.toString(), table)) {
            try {
                rs = query("SELECT * FROM " + table + " HAVING UUID = " + "\'" + uuid.toString() + "\';");
                rs.next();
                String discordId = rs.getString("DiscordId");
                String minecraftName = rs.getString("MinecraftName");
                return new UserRecord(discordId, minecraftName, uuid.toString());
            } catch (Exception e) {
                DiscordAllowList.LOGGER.error("Error getting user data from database!");
                e.printStackTrace();
            }
        }
        return null;
    }

    public UserRecord getByName(String name) {
        ResultSet rs;

        String table = MySQLConfig.table.get();

        if (itemExists("MinecraftName", name, table)) {
            try {
                rs = query("SELECT * FROM " + table + " HAVING MinecraftName = " + "'" + name + "';");
                rs.next();
                String discordId = rs.getString("DiscordId");
                String uuid = rs.getString("UUID");
                return new UserRecord(discordId, name, uuid);
            } catch (Exception e) {
                DiscordAllowList.LOGGER.error("Error getting user data from database!");
                e.printStackTrace();
            }
        }
        return null;
    }
}
