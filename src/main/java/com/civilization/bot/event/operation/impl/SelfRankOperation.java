package com.civilization.bot.event.operation.impl;

import javax.transaction.NotSupportedException;

import net.dv8tion.jda.core.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.model.UserRank;
import com.civilization.service.UserService;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component("selfRankOperation")
public class SelfRankOperation implements EventOperation {

    @Autowired
    private UserService userService;

    @Override
    public String execute(MessageReceivedEvent event) {
        return getUserRankMessage(userService.findUserRank(getAuthorName(event)));
    }

    private String getUserRankMessage(UserRank userRank) {
        return userRank.getUsername() +
                " rating: " + userRank.getRating() +
                ", games: " + userRank.getGamesCount() +
                ", wins: " + userRank.getWins() +
                ", leaves: " + userRank.getLeaves();
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String getAuthorName(MessageReceivedEvent event) {
        return event.getAuthor().getName();
    }
}
