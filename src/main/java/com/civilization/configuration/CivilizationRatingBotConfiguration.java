package com.civilization.configuration;

import com.civilization.bot.CivilizationRatingBot;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.service.RatingTableService;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
public class CivilizationRatingBotConfiguration {

    private static final int UPDATE_RATING_TABLE_DELAY = 600000;
    private static final int UPDATE_RATING_TABLE_PERIOD = 600000;

    @Value("${bot.token}")
    private String botToken;

    @Autowired
    @DiscordMessageListenerQualifier
    private List<ListenerAdapter> listeners;

    @Autowired
    private RatingTableService ratingTableService;

    @Bean(name = "CivilizationRatingBotInstance")
    public JDA getCivilizationRatingBot() throws LoginException {
        JDA bot = new CivilizationRatingBot().initBot(botToken);
        bot.addEventListener(new ListenerAdapter() {
            @Override
            public void onReady(ReadyEvent event) {
                ratingTableService.drawTable(bot);
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        ratingTableService.drawTable(bot);
                    }
                }, UPDATE_RATING_TABLE_DELAY, UPDATE_RATING_TABLE_PERIOD);
            }});

        listeners.forEach(bot::addEventListener);
        return bot;
    }

}
