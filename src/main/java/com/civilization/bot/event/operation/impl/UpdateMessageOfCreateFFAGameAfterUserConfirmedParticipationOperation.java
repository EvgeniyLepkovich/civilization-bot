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

    private static final String FOOTER_MESSAGE_PATTERN_EN =
            "please, confirm participation putting {gameId}+ in game chat\n" +
                    "you can decline game putting {gameId}- in chat";

    private static final String FOOTER_MESSAGE_PATTERN_RU =
            "пожалуйста, подтвердите участие написав {gameId}+ в чате игры\n" +
                    "вы можете отклонить игру написав {gameId}- в чат";

    @Autowired
    private CreatedGameMessagesCache createdGameMessagesCache;

    public void updateGameMessageEn(ActiveGame activeGame) throws RateLimitedException {
        Message message = createdGameMessagesCache.getMessage(activeGame.getId());
        String gameId = String.valueOf(activeGame.getId());

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("ffa game №" + gameId + " was created")
                .setColor(Color.green);

        activeGame.getUserActiveGames()
                .stream()
                .sorted(Comparator.comparing(uag -> uag.getUser().getUsername()))
                .forEach(uag -> builder.addField("@" + uag.getUser().getUsername(), "current rating: " +
                        uag.getUser().getRating() + "\nIs ready: " + toEmojy(uag.isGameConfirmed()), true));
        String footerMessage = FOOTER_MESSAGE_PATTERN_EN.replaceAll("\\{gameId}", gameId);
        MessageEmbed newMessageContent = builder.setFooter(footerMessage, null).build();

        Message updatedMessage = message.editMessage(newMessageContent).complete(false);
        createdGameMessagesCache.putMessage(gameId, updatedMessage);
    }

    public void updateGameMessageRu(ActiveGame activeGame) throws RateLimitedException {
        Message message = createdGameMessagesCache.getMessage(activeGame.getId());
        String gameId = String.valueOf(activeGame.getId());

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("ффа игра №" + gameId + " была создана")
                .setColor(Color.green);

        activeGame.getUserActiveGames()
                .stream()
                .sorted(Comparator.comparing(uag -> uag.getUser().getUsername()))
                .forEach(uag -> builder.addField("@" + uag.getUser().getUsername(), "текущий рейтинг: " +
                        uag.getUser().getRating() + "\nГотовность: " + toEmojy(uag.isGameConfirmed()), true));
        String footerMessage = FOOTER_MESSAGE_PATTERN_RU.replaceAll("\\{gameId}", gameId);
        MessageEmbed newMessageContent = builder.setFooter(footerMessage, null).build();

        Message updatedMessage = message.editMessage(newMessageContent).complete(false);
        createdGameMessagesCache.putMessage(gameId, updatedMessage);
    }

    private String toEmojy(boolean isReady) {
        return isReady ? ":partying_face:" : ":rage:";
    }
}
