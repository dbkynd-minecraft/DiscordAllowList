package com.dbkynd.discordallowlist.commands.roles;

import com.dbkynd.discordallowlist.config.DiscordConfig;
import com.dbkynd.discordallowlist.discord.DiscordBot;
import com.dbkynd.discordallowlist.whitelist.WhiteList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class CommandRolesAdd implements Command<CommandSource> {
    public static final CommandRolesAdd CMD = new CommandRolesAdd();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("add")
                .then(Commands.argument("discord_role_id", StringArgumentType.string())
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "discord_role_id").toLowerCase();
        List<String> roles = DiscordConfig.getAllowedRoles();

        Thread thread = new Thread(() -> {
            JDA jda = DiscordBot.getJda();

            Role role = jda.getRoleById(id);
            if (role == null) {
                context.getSource().sendFailure(new StringTextComponent("\"" + id + "\" is not a role id the Discord bot can see. Do you need to add the bot to the server?"));
                return;
            }

            if (roles.contains(id)) {
                context.getSource().sendSuccess(new StringTextComponent("\"" + id + "\" is already in the allowed roles list."), false);
            } else {
                roles.add(id);
                DiscordConfig.storeAllowedRoles(roles);
                context.getSource().sendSuccess(new StringTextComponent("Added \"" + id + "\" to the allowed roles list."), true);
                WhiteList.reload();
            }
        });
        thread.start();

        return 0;
    }
}
