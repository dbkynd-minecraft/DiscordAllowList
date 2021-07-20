package com.dbkynd.discordallowlist.commands;

import com.dbkynd.discordallowlist.whitelist.WhiteList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CommandReload implements Command<CommandSource> {

    public static final CommandReload CMD = new CommandReload();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("reload").executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Thread thread = new Thread(() -> {
            try {
                WhiteList.reload();
                context.getSource().sendSuccess(new StringTextComponent("Allow list reloaded."), false);
            } catch (Exception e) {
                e.printStackTrace();
                context.getSource().sendFailure(new StringTextComponent("There was an error reloading the allow list."));
            }
        });
        thread.start();
        return 0;
    }
}
