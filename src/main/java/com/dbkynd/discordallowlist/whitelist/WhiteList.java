package com.dbkynd.discordallowlist.whitelist;

import com.dbkynd.discordallowlist.DiscordAllowList;
import com.dbkynd.discordallowlist.config.BypassConfig;
import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.dbkynd.discordallowlist.handlers.ServerStartHandler;
import com.dbkynd.discordallowlist.http.WebRequest;
import com.dbkynd.discordallowlist.mojang.MojangJSON;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.server.management.WhitelistEntry;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

public class WhiteList {
    private static final Logger LOGGER = DiscordAllowList.LOGGER;

    public static void startTimer() {
        new Timer().scheduleAtFixedRate(new reloadSchedule(), 0, 1000 * 60 * 30);
    }

    public static void reload() {
        Thread asyncKick = new Thread(() -> {
            // LOGGER.info("Running whitelist reload script.");
            List<String> databaseIds = new ArrayList<>();
            List<String> databaseNames = new ArrayList<>();

            try {
                ResultSet rs = DiscordAllowList.getSql().getAll();

                while (rs.next()) {
                    String discordId = rs.getString("DiscordId");
                    String minecraftName = rs.getString("MinecraftName");
                    databaseIds.add(discordId);
                    databaseNames.add(minecraftName.toLowerCase());
                }

                List<String> currentWhitelist = getWhiteList();
                List<String> bypassNames = BypassConfig.getBypassNames();

                // Remove any whitelisted names that are no longer in the database and are not bypassed
                for (String name : currentWhitelist) {
                    if (!databaseNames.contains(name) && !bypassNames.contains(name)) {
                        LOGGER.info("Removing " + name + " from whitelist. Not in database and not bypassed.");
                        remove(name);
                    }
                }

                // Refresh the whitelist names
                currentWhitelist = getWhiteList();


                // Add any bypassed names that are not already in the whitelist
                for (String name : bypassNames) {
                    if (name.equals("")) continue;
                    if (!currentWhitelist.contains(name)) {
                        LOGGER.info("Adding " + name + " to whitelist. Not in whitelist and is bypassed.");
                        add(name);
                    }
                }

                // Refresh the whitelist names
                currentWhitelist = getWhiteList();

                List<String> allowedRoleList = DiscordConfig.getAllowedRoles();
                List<Guild> guilds = DiscordBot.getJda().getGuilds();

                // Check the Discord permissions for each id in the database and add/remove accordingly
                DatabaseLoop:
                for (int i = 0; i < databaseIds.size(); i++) {
                    String id = databaseIds.get(i);
                    String name = databaseNames.get(i);

                    User user = DiscordBot.getJda().retrieveUserById(id).complete();

                    for (Guild guild : guilds) {
                        Member member = guild.retrieveMember(user).complete();
                        if (member == null) continue;

                        List<Role> roles = member.getRoles();

                        for (Role role : roles) {
                            if (allowedRoleList.contains(role.getId())) {
                                if (!currentWhitelist.contains(name)) {
                                    LOGGER.info("Adding " + name + " to whitelist. Not in whitelist, in database, has role.");
                                    add(name);
                                }
                                continue DatabaseLoop;
                            }
                        }
                    }
                    if (currentWhitelist.contains(name)) {
                        LOGGER.info("Removing " + name + " from whitelist. In whitelist, in database, does not have role.");
                        remove(name);
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Error reloading the whitelist: " + e.getMessage());
            }

            // LOGGER.info("Done reloading whitelist.");
        });
        asyncKick.start();
    }

    private static void add(String name) {
        WebRequest request = new WebRequest();
        MojangJSON data = request.getMojangData(name);
        if (data == null) return;

        JsonObject json = new JsonObject();
        json.addProperty("name", data.getName());
        json.addProperty("uuid", data.getUUID().toString());

        WhitelistEntry entry = new WhitelistEntry(json);
        ServerStartHandler.server.getPlayerList().getWhiteList().add(entry);
    }

    private static void remove(String name) {
        WebRequest request = new WebRequest();
        MojangJSON data = request.getMojangData(name);
        if (data == null) return;

        JsonObject json = new JsonObject();
        json.addProperty("name", data.getName());
        json.addProperty("uuid", data.getUUID().toString());

        WhitelistEntry entry = new WhitelistEntry(json);

        ServerStartHandler.server.getPlayerList().getWhiteList().remove(entry);
    }

    private static List<String> getWhiteList() {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(ServerStartHandler.server.getPlayerList().getWhiteListNames()));
        return names.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public static class reloadSchedule extends TimerTask {
        @Override
        public void run() {
            reload();
        }
    }
}
