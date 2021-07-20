package com.dbkynd.discordallowlist.commands.channels;

import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class CommandChannelsAdd implements Command<CommandSource> {
    public static final CommandChannelsAdd CMD = new CommandChannelsAdd();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("add")
                .then(Commands.argument("discord_channel_id", StringArgumentType.string())
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "discord_channel_id").toLowerCase();
        List<String> channels = DiscordConfig.getAllowedChannels();

        Thread thread = new Thread(() -> {
            JDA jda = DiscordBot.getJda();

            GuildChannel channel = jda.getGuildChannelById(id);
            if (channel == null) {
                context.getSource().sendFailure(new StringTextComponent("\"" + id + "\" is not a channel id the Discord bot has access to. Do you need to add the bot to the server?"));
                return;
            }

            if (channels.contains(id)) {
                context.getSource().sendSuccess(new StringTextComponent("\"" + id + "\" is already in the allowed channels list."), false);
            } else {
                channels.add(id);
                DiscordConfig.storeAllowedChannels(channels);
                context.getSource().sendSuccess(new StringTextComponent("Added \"" + id + "\" to the allowed channels list."), false);
            }
        });
        thread.start();
        return 0;
    }
}
