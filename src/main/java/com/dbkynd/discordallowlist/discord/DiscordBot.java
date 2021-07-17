package com.dbkynd.discordallowlist.discord;

import com.dbkynd.discordallowlist.DiscordAllowList;
import com.dbkynd.discordallowlist.config.DiscordConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;


public class DiscordBot extends ListenerAdapter {
    private static final Logger LOGGER = DiscordAllowList.LOGGER;
    private static JDA jda;

    public static void main(String[] args) throws LoginException {
        LOGGER.info("Initializing Discord Bot...");

        jda = JDABuilder.createDefault(DiscordConfig.botToken.get(), GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new DiscordBot())
                .setActivity(Activity.playing("/addme"))
                .build();

        String link = "https://discordapp.com/oauth2/authorize?permissions=0&scope=applications.commands%20bot&client_id=" + jda.getSelfUser().getId();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("addme", "Add yourself to the Minecraft server allow list.")
                        .addOptions(new OptionData(STRING, "ign", "Your Minecraft in-game name.")
                                .setRequired(true))
        );

        commands.queue();
        LOGGER.info("Discord connection successful!");
        LOGGER.info("You can add this bot to Discord using this link: " + link);
    }

    public static JDA getJda() {
        return jda;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        if (event.getName().equals("addme")) {
            AddMeCommandHandler.action(event, event.getOption("ign").getAsString());
        }
    }
}
