package com.dbkynd.discordallowlist.commands.bypass;

import com.dbkynd.discordallowlist.config.BypassConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CommandBypassList implements Command<CommandSource> {
    public static final CommandBypassList CMD = new CommandBypassList();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("list").executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new StringTextComponent("Bypassed Users: " + BypassConfig.getBypassNames()), false);
        return 0;
    }
}
