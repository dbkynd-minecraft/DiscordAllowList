package com.dbkynd.discordallowlist.handlers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerJoinHandler {
  @SubscribeEvent
  public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
    player.connection.disconnect(ITextComponent.nullToEmpty("You are not whitelisted on this server!"));
  }
}
