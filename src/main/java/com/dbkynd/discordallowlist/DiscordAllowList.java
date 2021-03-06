package com.dbkynd.discordallowlist;

import com.dbkynd.discordallowlist.config.Config;
import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.config.MySQLConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.dbkynd.discordallowlist.handlers.ServerStartHandler;
import com.dbkynd.discordallowlist.sql.MySQLConnection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = DiscordAllowList.MODID)
public class DiscordAllowList {
    public static final String MODID = "discordallowlist";
    public static final Logger LOGGER = LogManager.getLogger();

    private static MySQLConnection sql;

    public DiscordAllowList() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config);
        Config.loadConfig(Config.config, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml").toString());

        if (MySQLConfig.host.get().isEmpty()) {
            LOGGER.error("You have not specified a MySql host. Check Configs.");
            return;
        } else if (MySQLConfig.port.get().isEmpty()) {
            LOGGER.error("You have not specified a MySql port. Check Configs.");
            return;
        } else if (MySQLConfig.database.get().isEmpty()) {
            LOGGER.error("You have not specified a MySql database. Check Configs.");
            return;
        } else if (MySQLConfig.table.get().isEmpty()) {
            LOGGER.error("You have not specified a MySql table. Check Configs.");
            return;
        } else if (MySQLConfig.username.get().isEmpty()) {
            LOGGER.error("You have not specified a MySql username. Check Configs.");
            return;
        } else if (MySQLConfig.password.get().isEmpty()) {
            LOGGER.error("You have not specified a MySql password. Check Configs.");
            return;
        } else if (DiscordConfig.botToken.get().isEmpty()) {
            LOGGER.error("You have not specified a Discord App Bot token. Check Configs.");
            return;
        }

        try {
            sql = new MySQLConnection();
            DiscordBot.main(null);
        } catch (Exception error) {
            LOGGER.error(error.getMessage());
            LOGGER.error("Unable to process allow lists.");
            return;
        }

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerStartHandler());
        LOGGER.info("Ready to process allow lists.");
    }

    public static MySQLConnection getSql() {
        return sql;
    }
}
