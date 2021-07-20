package com.dbkynd.discordallowlist.commands.bypass;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandBypass {
    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("bypass")
                .then(CommandBypassAdd.register(dispatcher))
                .then(CommandBypassRemove.register(dispatcher))
                .then(CommandBypassList.register(dispatcher));
    }
}
