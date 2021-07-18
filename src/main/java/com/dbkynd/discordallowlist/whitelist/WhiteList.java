package com.dbkynd.discordallowlist.whitelist;

import com.dbkynd.discordallowlist.DiscordAllowList;
import com.dbkynd.discordallowlist.config.BypassConfig;
import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WhiteList {
    private static final Logger LOGGER = DiscordAllowList.LOGGER;

    private static final ArrayList<String> tempwhitelist = new ArrayList<>();

    public static void startTimer() {
        // Run every 4 hours
        // new Timer().scheduleAtFixedRate(new reloadSchedule(), 0, 1000 * 60 * 60 * 4);
        new Timer().scheduleAtFixedRate(new reloadSchedule(), 0, 1000 * 30);
    }

    public static void reload() {
        Thread asyncKick = new Thread(() -> {
            LOGGER.info("Running whitelist reload script.");
            List<String> databaseIds = new ArrayList<>();
            List<String> databaseNames = new ArrayList<>();

            try {
                ResultSet rs = DiscordAllowList.getSql().getAll();

                while (rs.next()) {
                    String discordId = rs.getString("DiscordId");
                    String minecraftName = rs.getString("MinecraftName");
                    databaseIds.add(discordId);
                    databaseNames.add(minecraftName);
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

            LOGGER.info("Done reloading whitelist.");
        });
        asyncKick.start();
    }

    private static void add(String name) {
        tempwhitelist.add(name);
    }

    private static void remove(String name) {
        tempwhitelist.remove(name);
    }

    private static List<String> getWhiteList() {
        return tempwhitelist;
    }


    public static class reloadSchedule extends TimerTask {
        @Override
        public void run() {
            reload();
        }
    }
}
