package com.dbkynd.discordallowlist.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;

public class DiscordConfig {
    public static ForgeConfigSpec.ConfigValue<String> botToken;
    public static ForgeConfigSpec.ConfigValue<String> allowedChannels;
    public static ForgeConfigSpec.ConfigValue<String> allowedRoles;
    public static ForgeConfigSpec.ConfigValue<String> kickMessage;

    public static void init(ForgeConfigSpec.Builder common) {
        common.comment("Discord Config");
        botToken = common.comment("Discord App Bot Token").define("discord.token", "");
        allowedChannels = common.comment("Comma delimited list of allowed channel ids to respond to slash commands in.").define("discord.allowed_channels", "000000000000,000000000000");
        allowedRoles = common.comment("Comma delimited list of allowed role ids to allow server access to.").define("discord.allowed_roles", "000000000000,000000000000");
        kickMessage = common.comment("The kick message to send to the player if they are not registered of have the proper role.").define("discord.kick_message", "You are not whitelisted on this server!");
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
