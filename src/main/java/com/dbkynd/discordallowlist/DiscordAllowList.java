package com.dbkynd.discordallowlist;

import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.dbkynd.discordallowlist.handlers.PlayerJoinHandler;
import com.dbkynd.discordallowlist.sql.MySQLConnection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

@Mod("discordallowlist")
public class DiscordAllowList
{
    public static final Logger LOGGER = LogManager.getLogger();

    private static String botToken = ""; // TODO: Get from config file
    private static MySQLConnection sql;
    private static String sqlTable = "AllowList";

    public DiscordAllowList() throws LoginException {
        // Register ourselves for server and other game events we are interested in
        // MinecraftForge.EVENT_BUS.register(this);

      // TODO: Get from config file
      sql = new MySQLConnection("", "", "", "", "");
      sql.connect();
      // Create table if does not exist
      if (!sql.tableExists(sqlTable)) {
        LOGGER.info(sqlTable + " table not found. Creating new table...");
        sql.update("CREATE TABLE " + sqlTable + " (DiscordID CHAR(18), MinecraftName VARCHAR(16), UUID CHAR(36), PRIMARY KEY (DiscordID));");
        // Ensure table was created before saying so
        if (sql.tableExists(sqlTable)) {
          LOGGER.info(sqlTable + " table created");
        }
      }

     DiscordBot.start();

      MinecraftForge.EVENT_BUS.register(new PlayerJoinHandler());
      LOGGER.info("Ready");
    }

    public static String getBotToken() {
      return botToken;
    }

    public static MySQLConnection getSql() {
      return sql;
    }

    public static String getTableName() {
      return sqlTable;
    }
}
