package com.civilization.bot.event.operation.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;

import com.civilization.cache.CreatedGameMessagesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.model.ActiveGame;
import com.civilization.service.ActiveGameService;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

@Component("declineParticipationInFFAGameOperation")
public class DeclineParticipationInFFAGameOperation implements EventOperation {

    private static final String PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN_EN =
            "@{participant} declined the game number: {gameId}!\n" +
            "{participants} - your game was canceled";

    private static final String PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN_RU =
            "@{participant} отклонил игру под номером: {gameId}!\n" +
            "{participants} - ваша игра была отклонена";

    private static final String ERROR_DECLINED_MESSAGE_PATTERN_EN =
            "Had some troubles declining the game, please contact admin: @{admin}";

    private static final String ERROR_DECLINED_MESSAGE_PATTERN_RU =
            "возникли проблемы с отказом игры, пожалуйста обратитесь к адмиристратору: @{admin}";

    @Autowired
    private ActiveGameService activeGameService;
    @Autowired
    private UpdateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation updateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation;

    @Value("${discord.administrator.name}")
    private String administratorName;

    @Autowired
    private CreatedGameMessagesCache cacheLanguage;

    @Override
    public String execute(MessageReceivedEvent event) throws RateLimitedException {
        String triggeredEventOwner = getTriggeredEventOwner(event);
        String message = event.getMessage().getContentDisplay();
        boolean isEnglish = cacheLanguage.getLanguage(getGameId(message).toString()).contains("isEnglish");
        Optional<ActiveGame> activeGame = activeGameService.setUserDeclinedGame(getGameId(message), triggeredEventOwner);

        if (activeGame.isPresent()) {
            String resultMessage = getParticipantDeclinedGameMessage(activeGame.get(), triggeredEventOwner, isEnglish);
            if (isEnglish) {
                updateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation.updateGameMessageEn(activeGame.get());
            } else {
                updateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation.updateGameMessageRu(activeGame.get());
            }
            return resultMessage;
        }
        return getErrorDeclinedMessage(isEnglish);
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String getErrorDeclinedMessage(boolean isEnglish) {
        String errorDeclineMessage = isEnglish ? ERROR_DECLINED_MESSAGE_PATTERN_EN : ERROR_DECLINED_MESSAGE_PATTERN_RU;
        return errorDeclineMessage
                .replace("{admin}", administratorName);
    }

    private String getParticipantDeclinedGameMessage(ActiveGame activeGame, String triggeredEventOwner, boolean isEnglish) {
        String participantDeclineGameMessage = isEnglish ?
                PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN_EN :
                PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN_RU;
        return participantDeclineGameMessage
                .replace("{participant}", triggeredEventOwner)
                .replace("{gameId}", String.valueOf(activeGame.getId()))
                .replace("{participants}", getListOfPlayers(activeGame));
    }

    private Long getGameId(String message) {
        return Long.valueOf(message.replaceAll("\\D", ""));
    }

    private String getListOfPlayers(ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .map(uag -> "@" + uag.getUser().getUsername())
                .collect(Collectors.joining(", "));
    }

    private String getTriggeredEventOwner(MessageReceivedEvent event) {
        return event.getMessage().getAuthor().getName();
    }
}
