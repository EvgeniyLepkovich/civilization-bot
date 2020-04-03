package com.civilization.configuration;

import java.util.List;

import com.civilization.bot.CivilizationRatingBot;
import com.civilization.bot.event.ConfirmParticipationInFFAGameMessageListener;
import com.civilization.bot.event.CreateFFAGameMessageListener;
import com.civilization.bot.event.CreateFFAReportMessageListener;
import com.civilization.bot.event.DeclineParticipationInFFAGameMessageListener;
import com.civilization.bot.event.SelfRankMessageListener;
import com.civilization.bot.event.UserRankMessageListener;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class CivilizationRatingBotConfiguration {

    @Value("${bot.token}")
    private String botToken;

    @Autowired
    @DiscordMessageListenerQualifier
    private List<ListenerAdapter> listeners;

    @Bean(name = "CivilizationRatingBotInstance")
    public JDA getCivilizationRatingBot() throws LoginException {
        JDA bot = new CivilizationRatingBot()
                .initBot(botToken);
        listeners.forEach(bot::addEventListener);
        return bot;
    }
}
