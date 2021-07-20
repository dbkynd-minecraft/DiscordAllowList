package com.dbkynd.discordallowlist.commands.bypass;

import com.dbkynd.discordallowlist.config.BypassConfig;
import com.dbkynd.discordallowlist.http.WebRequest;
import com.dbkynd.discordallowlist.mojang.MojangJSON;
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

public class CommandBypassRemove implements Command<CommandSource> {
    public static final CommandBypassRemove CMD = new CommandBypassRemove();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("remove")
                .then(Commands.argument("minecraft_name", StringArgumentType.string())
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "minecraft_name").toLowerCase();
        List<String> names = BypassConfig.getBypassNames();

        Thread thread = new Thread(() -> {
            MojangJSON mojangUser = new WebRequest().getMojangData(name);
            if (mojangUser == null) {
                context.getSource().sendFailure(new StringTextComponent("\"" + name + "\" is not a registered Mojang account name."));
                return;
            }

            if (!names.contains(name)) {
                context.getSource().sendSuccess(new StringTextComponent("\"" + mojangUser.getName() + "\" was not in the bypass list."), false);
            } else {
                names.remove(name);
                BypassConfig.bypassNames.set(String.join(",", names));
                context.getSource().sendSuccess(new StringTextComponent("Removed \"" + mojangUser.getName() + "\" from the bypass list."), true);
                WhiteList.reload();
            }
        });
        thread.start();
        return 0;
    }
}
