package com.civilization.bot.event.operation.impl;

import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.model.ActiveGame;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Comparator;

@Component
public class UpdateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation {

    private static final String FOOTER_MESSAGE_PATTERN_EN =
            "please, confirm participation putting {gameId}+ in game chat\n" +
                    "you can decline game putting {gameId}- in chat";

    private static final String FOOTER_MESSAGE_PATTERN_RU =
            "пожалуйста, подтвердите участие написав {gameId}+\n" +
                    "вы можете отклонить игру написав {gameId}-";

    public void updateGameMessageEn(ActiveGame activeGame) throws RateLimitedException {
        CreatedGameMessagesCache createdGameMessagesCache = CreatedGameMessagesCache.getInstance();
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
        CreatedGameMessagesCache createdGameMessagesCache = CreatedGameMessagesCache.getInstance();
        Message message = createdGameMessagesCache.getMessage(activeGame.getId());
        String gameId = String.valueOf(activeGame.getId());

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("ффа игра №" + gameId + " была создана")
                .setColor(Color.green);

        activeGame.getUserActiveGames()
                .stream()
                .sorted(Comparator.comparing(uag -> uag.getUser().getUsername()))
                .forEach(uag -> builder.addField("@" + uag.getUser().getUsername(), "текущий рейтинг: " +
                        uag.getUser().getRating() + "\nготовность: " + toEmojy(uag.isGameConfirmed()), true));
        String footerMessage = FOOTER_MESSAGE_PATTERN_RU.replaceAll("\\{gameId}", gameId);
        MessageEmbed newMessageContent = builder.setFooter(footerMessage, null).build();

        Message updatedMessage = message.editMessage(newMessageContent).complete(false);
        createdGameMessagesCache.putMessage(gameId, updatedMessage);
    }

    private String toEmojy(boolean isReady) {
        return isReady ? ":partying_face:" : ":rage:";
    }
}
