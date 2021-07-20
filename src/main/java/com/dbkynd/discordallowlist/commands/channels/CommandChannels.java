package com.dbkynd.discordallowlist.commands.channels;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandChannels {
    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("channels")
                .then(CommandChannelsAdd.register(dispatcher))
                .then(CommandChannelsRemove.register(dispatcher))
                .then(CommandChannelsList.register(dispatcher));
    }
}
