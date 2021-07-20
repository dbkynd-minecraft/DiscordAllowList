package com.dbkynd.discordallowlist.handlers;

import com.dbkynd.discordallowlist.commands.ModCommands;
import com.dbkynd.discordallowlist.whitelist.WhiteList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ServerStartHandler {
    public static String SERVER_FILEPATH;
    public static MinecraftServer server;

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        server = event.getServer();
        SERVER_FILEPATH = server.getServerDirectory().getPath();
        ModCommands.register(event.getServer().getCommands().getDispatcher());
/*        if (server.getPlayerList().isUsingWhitelist()) {
            DiscordAllowList.LOGGER.warn("The server's whitelist is not enabled. Please enable it for this mod to function properly.");
        }*/
        WhiteList.startTimer();
    }
}
