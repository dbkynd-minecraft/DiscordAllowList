package com.dbkynd.discordallowlist.commands.roles;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandRoles {
    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("roles")
                .then(CommandRolesAdd.register(dispatcher))
                .then(CommandRolesRemove.register(dispatcher))
                .then(CommandRolesList.register(dispatcher));
    }
}
