package com.dbkynd.discordallowlist.commands;

import com.dbkynd.discordallowlist.config.BypassConfig;
import com.dbkynd.discordallowlist.whitelist.WhiteList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CommandBypassAdd implements Command<CommandSource> {
    public static final CommandBypassAdd CMD = new CommandBypassAdd();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("bypass")
                .requires(cs -> cs.hasPermission(3))
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(CMD)));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name").toLowerCase();
        List<String> names = BypassConfig.getBypassNames();

        if (names.contains(name)) {
            context.getSource().sendSuccess(ITextComponent.nullToEmpty("\"" + name + "\" is already in the bypass list."), false);
        } else {
            names.add(name);
            BypassConfig.bypassNames.set(String.join(",", names));
            WhiteList.reload();
            context.getSource().sendSuccess(ITextComponent.nullToEmpty("Added \"" + name + "\" to the bypass list."), false);
        }
        return 0;
    }
}
