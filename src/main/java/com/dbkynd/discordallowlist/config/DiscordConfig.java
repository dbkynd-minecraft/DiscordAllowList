package com.dbkynd.discordallowlist.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordConfig {
    public static ForgeConfigSpec.ConfigValue<String> botToken;
    public static ForgeConfigSpec.ConfigValue<String> allowedChannels;
    public static ForgeConfigSpec.ConfigValue<String> allowedRoles;
    public static ForgeConfigSpec.ConfigValue<String> embedMessage;

    public static void init(ForgeConfigSpec.Builder common) {
        common.comment("Discord Config");
        botToken = common.comment("Discord App Bot Token").define("discord.token", "");
        allowedChannels = common.comment("Comma delimited list of allowed channel ids to respond to slash commands in.").define("discord.allowed_channels", "");
        allowedRoles = common.comment("Comma delimited list of allowed role ids to allow server access to.").define("discord.allowed_roles", "");
        embedMessage = common.comment("The message sent to a user in discord after successfully running the /addme command at the bottom of the embed.").define("discord.embed_message", "has been added to the Minecraft user database!");
    }

    public static ArrayList<String> getAllowedChannels() {
        String[] channels = allowedChannels.get().split("(\\s+)?,(\\s+)?");
        return new ArrayList<>(Arrays.asList(channels));
    }

    public static void storeAllowedChannels(List<String> channels) {
        List<String> filteredChannels = Config.removeEmpty(channels);
        allowedChannels.set(String.join(",", filteredChannels));
    }

    public static ArrayList<String> getAllowedRoles() {
        String[] roles = allowedRoles.get().split("(\\s+)?,(\\s+)?");
        return new ArrayList<>(Arrays.asList(roles));
    }

    public static void storeAllowedRoles(List<String> roles) {
        List<String> filteredRoles = Config.removeEmpty(roles);
        allowedRoles.set(String.join(",", filteredRoles));
    }
}
