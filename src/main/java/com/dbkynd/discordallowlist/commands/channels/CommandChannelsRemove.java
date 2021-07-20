package com.dbkynd.discordallowlist.commands.channels;

import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class CommandChannelsRemove implements Command<CommandSource> {
    public static final CommandChannelsRemove CMD = new CommandChannelsRemove();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("remove")
                .then(Commands.argument("discord_channel_id", StringArgumentType.string())
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "discord_channel_id").toLowerCase();
        List<String> channels = DiscordConfig.getAllowedChannels();

        if (!channels.contains(id)) {
            context.getSource().sendSuccess(new StringTextComponent("\"" + id + "\" was not in the allowed channels list."), false);
        } else {
            channels.remove(id);
            DiscordConfig.storeAllowedChannels(channels);
            context.getSource().sendSuccess(new StringTextComponent("Removed \"" + id + "\" from the allowed channels list."), false);
        }
        return 0;
    }
}
