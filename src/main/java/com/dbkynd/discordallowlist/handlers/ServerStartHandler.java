package com.dbkynd.discordallowlist.handlers;

import com.dbkynd.discordallowlist.commands.ModCommands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ServerStartHandler {

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.register(event.getServer().getCommands().getDispatcher());
    }
}
