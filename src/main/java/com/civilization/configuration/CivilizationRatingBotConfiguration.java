package com.civilization.configuration;

import com.civilization.bot.CivilizationRatingBot;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.service.RatingTableService;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

@Configuration
public class CivilizationRatingBotConfiguration {

    private static final int UPDATE_RATING_TABLE_DELAY = 600000;
    private static final int UPDATE_RATING_TABLE_PERIOD = 600000;
    private static final int LIMIT = 50;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.rating-channel-id}")
    private Long ratingChannelId;

    @Autowired
    @DiscordMessageListenerQualifier
    private List<ListenerAdapter> listeners;

    @Autowired
    private RatingTableService ratingTableService;

    @Bean(name = "CivilizationRatingBotInstance")
    public JDA getCivilizationRatingBot() throws LoginException {
        JDA bot = new CivilizationRatingBot().initBot(botToken);
        Consumer<JDA> drawRatingTableConsumer = this::drawRatingTable;
        bot.addEventListener(new ListenerAdapter() {
            @Override
            public void onReady(ReadyEvent event) {
                drawRatingTableConsumer.accept(bot);
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        drawRatingTableConsumer.accept(bot);
                    }
                }, UPDATE_RATING_TABLE_DELAY, UPDATE_RATING_TABLE_PERIOD);
            }});

        listeners.forEach(bot::addEventListener);
        return bot;
    }

    private void drawRatingTable(JDA botInstance) {
        String usersRankTable = ratingTableService.getUsersRankTable(LIMIT);
        TextChannel textChannel = botInstance.getTextChannelById(ratingChannelId);
        Optional<List<Message>> oldMessages = getOldMessages(textChannel);
        if (oldMessages.isPresent() && oldMessages.get().size() > 0) {
            oldMessages.get().get(0).editMessage("```" + usersRankTable + "```").queue();
        } else {
            textChannel.sendMessage("```" + usersRankTable + "```").queue();
        }
    }

    private Optional<List<Message>> getOldMessages(TextChannel textChannel) {
        return Optional.ofNullable(textChannel.getHistory()).map(h -> h.retrievePast(1).complete());
    }
}
