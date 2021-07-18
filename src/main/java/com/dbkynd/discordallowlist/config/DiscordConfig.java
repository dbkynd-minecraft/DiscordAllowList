package com.dbkynd.discordallowlist.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;

public class DiscordConfig {
    public static ForgeConfigSpec.ConfigValue<String> botToken;
    public static ForgeConfigSpec.ConfigValue<String> allowedChannels;
    public static ForgeConfigSpec.ConfigValue<String> allowedRoles;
    public static ForgeConfigSpec.ConfigValue<String> embedMessage;

    public static void init(ForgeConfigSpec.Builder common) {
        common.comment("Discord Config");
        botToken = common.comment("Discord App Bot Token").define("discord.token", "");
        allowedChannels = common.comment("Comma delimited list of allowed channel ids to respond to slash commands in.").define("discord.allowed_channels", "000000000000,000000000000");
        allowedRoles = common.comment("Comma delimited list of allowed role ids to allow server access to.").define("discord.allowed_roles", "000000000000,000000000000");
        embedMessage = common.comment("The message sent to a user in discord after successfully running the /addme command at the bottom of the embed.").define("discord.embed_message", "has been added to the Minecraft user database!");
    }

    public static ArrayList<String> getAllowedChannels() {
        String[] channels = allowedChannels.get().split("(\\s+)?,(\\s+)?");
        return new ArrayList<>(Arrays.asList(channels));
    }

    public static ArrayList<String> getAllowedRoles() {
        String[] roles = allowedRoles.get().split("(\\s+)?,(\\s+)?");
        return new ArrayList<>(Arrays.asList(roles));
    }
}
