package com.dbkynd.discordallowlist.commands;

import com.dbkynd.discordallowlist.commands.bypass.CommandBypass;
import com.dbkynd.discordallowlist.commands.channels.CommandChannels;
import com.dbkynd.discordallowlist.commands.roles.CommandRoles;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("discordallowlist")
                        .requires(cs -> cs.hasPermission(3))
                        .then(CommandReload.register(dispatcher))
                        .then(CommandBypass.register(dispatcher))
                        .then(CommandChannels.register(dispatcher))
                        .then(CommandRoles.register(dispatcher))
        );
    }
}
