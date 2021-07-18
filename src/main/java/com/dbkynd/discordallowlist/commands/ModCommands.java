package com.dbkynd.discordallowlist.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("discordallowlist")
                        .requires(cs -> cs.hasPermission(3))
                        .then(CommandBypassAdd.register(dispatcher))
                        .then(CommandBypassRemove.register(dispatcher))
                        .then(CommandReload.register(dispatcher))
        );
    }
}
