package com.civilization.configuration;

import com.civilization.dto.ScrappedActiveGameDTO;
import com.civilization.service.UserService;
import net.dv8tion.jda.core.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
public class AutoGameScrapConfiguration {

    private static final int ONE_WEEK = 604800000;

    @Autowired
    private UserService userService;
    @Autowired
    private JDA jda;

    //TODO: move to the configuration table
    @Value("${discord.channel.rating-games.channel-id}")
    private Long channelId;

    @PostConstruct
    public void initAutoScrapTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                List<ScrappedActiveGameDTO> games = userService.scrapOldGames();
                games
                        .forEach(game -> jda.getTextChannelById(channelId)
                                .sendMessage("```" + getMessageForScrappedGames(game) + "```").queue());
            }
        };

        new Timer().scheduleAtFixedRate(timerTask, 0, ONE_WEEK);
    }

    private String getMessageForScrappedGames(ScrappedActiveGameDTO game) {
        return "game №" + game.getGameId() + " was scrapped for players: " + game.getUsernamesAsString();
    }
}
