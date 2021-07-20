package com.dbkynd.discordallowlist.commands.roles;

import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.whitelist.WhiteList;
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

public class CommandRolesRemove implements Command<CommandSource> {
    public static final CommandRolesRemove CMD = new CommandRolesRemove();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("remove")
                .then(Commands.argument("discord_role_id", StringArgumentType.string())
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "discord_role_id").toLowerCase();
        List<String> roles = DiscordConfig.getAllowedRoles();

        if (!roles.contains(id)) {
            context.getSource().sendSuccess(new StringTextComponent("\"" + id + "\" was not in the allowed roles list."), false);
        } else {
            roles.remove(id);
            DiscordConfig.storeAllowedRoles(roles);
            context.getSource().sendSuccess(new StringTextComponent("Removed \"" + id + "\" from the allowed roles list."), true);
            WhiteList.reload();
        }
        return 0;
    }
}
