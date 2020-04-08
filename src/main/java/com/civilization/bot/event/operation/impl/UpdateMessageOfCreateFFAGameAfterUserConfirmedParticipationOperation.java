package com.civilization.bot.event.operation.impl;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.model.ActiveGame;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

@Component
public class UpdateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation {

    private static final String FOOTER_MESSAGE_PATTERN =
            "please, confirm participation putting {gameId}+ in game chat\n" +
                    "you can decline game putting {gameId}- in chat";

    @Autowired
    private CreatedGameMessagesCache createdGameMessagesCache;

    public void updateGameMessage(ActiveGame activeGame) throws RateLimitedException {
        Message message = createdGameMessagesCache.getMessage(activeGame.getId());
        String gameId = String.valueOf(activeGame.getId());

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("ffa game №" + gameId + " was created")
                .setColor(Color.green);

        activeGame.getUserActiveGames().forEach(uag -> builder.addField("@" + uag.getUser().getUsername(), "current rating: " + uag.getUser().getRating() + "\nIs ready: " + toEmojy(uag.isGameConfirmed()), true));
        String footerMessage = FOOTER_MESSAGE_PATTERN.replaceAll("\\{gameId}", gameId);
        MessageEmbed newMessageContent = builder.setFooter(footerMessage, null).build();

        Message updatedMessage = message.editMessage(newMessageContent).complete(false);
        createdGameMessagesCache.putMessage(gameId, updatedMessage);
    }

    private List<User> getAllUsers(ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .map(UserActiveGame::getUser)
                .collect(Collectors.toList());
    }

//    private String getSpaceIndent(List<User> users) {
//        String maxUsername = users.stream().map(User::getUsername).max(Comparator.comparingInt(String::length)).orElse("");
//        return IntStream.range(0, Math.abs(maxUsername.length())).mapToObj(o -> " ").collect(Collectors.joining());
//    }

    private String toEmojy(boolean isReady) {
        return isReady ? ":partying_face:" : ":rage:";
    }
}
