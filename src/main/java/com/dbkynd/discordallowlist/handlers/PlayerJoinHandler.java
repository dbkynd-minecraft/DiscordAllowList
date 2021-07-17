package com.dbkynd.discordallowlist.handlers;

import com.dbkynd.discordallowlist.DiscordAllowList;
import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.dbkynd.discordallowlist.sql.UserRecord;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerJoinHandler {
    private static final Logger LOGGER = DiscordAllowList.LOGGER;

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // kickIfNotAllowed((ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public void onConnect(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            kickIfNotAllowed((ServerPlayerEntity) event.getEntity());
        }
    }

    private void kickIfNotAllowed(ServerPlayerEntity player) {
        UUID uuid = player.getUUID();
        String playerName = player.getName().getString();

        Thread asyncKick = new Thread(() -> {
            UserRecord userRecord = DiscordAllowList.getSql().getByUuid(uuid);

            String[] allowedRoles = DiscordConfig.allowedRoles.get().split("(\\s+)?,(\\s+)?");
            List<String> allowedRoleList = Arrays.asList(allowedRoles);

            if (userRecord != null) {
                User user = DiscordBot.getJda().retrieveUserById(userRecord.getDiscordId()).complete();

                List<Guild> guilds = DiscordBot.getJda().getGuilds();

                for (Guild guild : guilds) {
                    Member member = guild.retrieveMember(user).complete();
                    List<Role> roles = member.getRoles();

                    for (Role role : roles) {
                        if (allowedRoleList.contains(role.getId())) {
                            LOGGER.info("[" + playerName + "] has the role: '" + role.getName() + "'");
                            return;
                        }
                    }
                }

                kickPlayer(player, playerName + " attempted to connect. Missing required role");
            } else {
                kickPlayer(player, playerName + " attempted to connect. Not registered");
            }

        });
        asyncKick.start();
    }

    private void kickPlayer(ServerPlayerEntity player, String message) {
        LOGGER.info(message);
        player.connection.disconnect(ITextComponent.nullToEmpty(DiscordConfig.kickMessage.get()));
    }
}
