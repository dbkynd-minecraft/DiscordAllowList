package com.dbkynd.discordallowlist;

import com.dbkynd.discordallowlist.config.Config;
import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.config.MySQLConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.dbkynd.discordallowlist.handlers.PlayerJoinHandler;
import com.dbkynd.discordallowlist.sql.MySQLConnection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

@Mod("discordallowlist")
public class DiscordAllowList {
    public static final Logger LOGGER = LogManager.getLogger();

    private static MySQLConnection sql;

    public DiscordAllowList() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config);
        Config.loadConfig(Config.config, FMLPaths.CONFIGDIR.get().resolve("discordAllowList-common.toml").toString());

        String host = MySQLConfig.host.get();
        String port = MySQLConfig.port.get();
        String database = MySQLConfig.database.get();
        String table = MySQLConfig.table.get();
        String username = MySQLConfig.username.get();
        String password = MySQLConfig.password.get();
        String token = DiscordConfig.botToken.get();

        if (host == null || host.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a MySql host. Check Configs.");
            return;
        } else if (port == null || port.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a MySql port. Check Configs.");
            return;
        } else if (database == null || database.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a MySql database. Check Configs.");
            return;
        } else if (table == null || table.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a MySql table. Check Configs.");
            return;
        } else if (username == null || username.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a MySql username. Check Configs.");
            return;
        } else if (password == null || password.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a MySql password. Check Configs.");
            return;
        } else if (token == null || token.equalsIgnoreCase("")) {
            LOGGER.error("You have not specified a Discord App Bot token. Check Configs.");
            return;
        }

        try {
            sql = new MySQLConnection();
            DiscordBot.main(null);
        } catch(Exception error) {
            LOGGER.error(error.getMessage());
            LOGGER.error("Unable to process allow lists.");
            return;
        }

        MinecraftForge.EVENT_BUS.register(new PlayerJoinHandler());
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Ready to process allow lists.");
    }

    public static MySQLConnection getSql() {
        return sql;
    }
}
