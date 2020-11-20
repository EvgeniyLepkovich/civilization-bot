package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.model.UserRank;
import com.civilization.service.RatingService;
import com.civilization.service.UserService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component("userRankOperation")
//TODO: combine with SelfRankOperation
public class UserRankOperation implements EventOperation {

    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;

    @Override
    public String execute(MessageReceivedEvent event) {
            return getUserRankMessage(ratingService.findUserRank(getTarget(event)));
     }

    private String getUserRankMessage(UserRank userRank) {
        return userRank.getUsername() +
                " rating: " + userRank.getRating() +
                ", games: " + userRank.getGamesCount() +
                ", wins: " + userRank.getWins() +
                ", leaves: " + userRank.getLeaves() +
                ", scrap: " + userRank.getScrap();
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String getTarget(MessageReceivedEvent event) {
        return event.getMessage().getContentDisplay().split("\\s+")[1].replace("@", "");
    }
}
