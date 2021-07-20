package com.dbkynd.discordallowlist.commands.channels;

import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CommandChannelsList implements Command<CommandSource> {
    public static final CommandChannelsList CMD = new CommandChannelsList();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("list").executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new StringTextComponent("Allowed Channels: " + DiscordConfig.getAllowedChannels()), false);
        return 0;
    }
}
