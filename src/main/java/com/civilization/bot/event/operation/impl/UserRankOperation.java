package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.model.UserRank;
import com.civilization.service.UserService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component("userRankOperation")
public class UserRankOperation implements EventOperation {

    @Autowired
    private UserService userService;

    @Override
    public String execute(MessageReceivedEvent event) {
        boolean isEnglish = event.getMessage().getContentDisplay().contains("!rank");
        if (isEnglish) {
            return getUserRankMessageEn(userService.findUserRank(getTarget(event)));
        } else {
            return getUserRankMessageRu(userService.findUserRank(getTarget(event)));
        }
     }

    private String getUserRankMessageEn(UserRank userRank) {
        return userRank.getUsername() +
                " rating: " + userRank.getRating() +
                ", games: " + userRank.getGamesCount() +
                ", wins: " + userRank.getWins() +
                ", leaves: " + userRank.getLeaves();
    }

    private String getUserRankMessageRu(UserRank userRank) {
        return userRank.getUsername() +
                " рейтинг: " + userRank.getRating() +
                ", игр: " + userRank.getGamesCount() +
                ", побед: " + userRank.getWins() +
                ", ливов: " + userRank.getLeaves();
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String getTarget(MessageReceivedEvent event) {
        return event.getMessage().getContentDisplay().split("\\s+")[1].replace("@", "");
    }
}
