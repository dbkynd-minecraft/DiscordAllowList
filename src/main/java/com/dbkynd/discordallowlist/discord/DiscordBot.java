package com.dbkynd.discordallowlist.discord;

import com.dbkynd.discordallowlist.DiscordAllowList;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.logging.log4j.Logger;
import static net.dv8tion.jda.api.interactions.commands.OptionType.*;
import javax.security.auth.login.LoginException;
import java.util.EnumSet;


public class DiscordBot extends ListenerAdapter {
  private static final Logger LOGGER = DiscordAllowList.LOGGER;

  public static void start() throws LoginException {
    LOGGER.info("Initializing Discord Bot...");

    JDA jda = JDABuilder.createLight(DiscordAllowList.getBotToken(), EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
      .addEventListeners(new DiscordBot())
      .build();

    String link = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=" + jda.getSelfUser().getId();

    CommandListUpdateAction commands = jda.updateCommands();

    commands.addCommands(
      new CommandData("addme", "Add yourself to the MC server allow list.")
        .addOptions(new OptionData(STRING, "ign", "Your Minecraft in-game name.")
          .setRequired(true))
    );

    commands.queue();
    LOGGER.info("Discord connection successful!");
    LOGGER.info("You can add this bot to Discord using this link: " + link);
  }

  @Override
  public void onSlashCommand(SlashCommandEvent event)
  {
    // Only accept commands from guilds
    if (event.getGuild() == null)
      return;
    if ("addme".equals(event.getName())) {
      AddMeCommandHandler.action(event, event.getOption("ign").getAsString());
    } else {
      event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
    }
  }
}
