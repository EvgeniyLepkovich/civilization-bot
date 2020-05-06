package com.civilization.bot.event.operation.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;

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

    private static final String PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN =
            "@{participant} declined the game number: {gameId}!\n" +
            "{participants} - your game was canceled";

    private static final String ERROR_DECLINED_MESSAGE_PATTERN =
            "Had some troubles declining the game, please contact admin: @{admin}";

    @Autowired
    private ActiveGameService activeGameService;
    @Autowired
    private UpdateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation updateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation;

    @Value("${discord.administrator.name}")
    private String administratorName;

    @Override
    public String execute(MessageReceivedEvent event) throws RateLimitedException {
        String triggeredEventOwner = getTriggeredEventOwner(event);
        String message = event.getMessage().getContentDisplay();
        boolean isEnglish = message.equals("isEnglish");
        Optional<ActiveGame> activeGame = activeGameService.setUserDeclinedGame(getGameId(message), triggeredEventOwner);

        if (activeGame.isPresent()) {
            String resultMessage = getParticipantDeclinedGameMessage(activeGame.get(), triggeredEventOwner);
            if (isEnglish) {
                updateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation.updateGameMessageEn(activeGame.get());
            } else {
                updateMessageOfCreateFFAGameAfterUserDeclinedParticipationOperation.updateGameMessageRu(activeGame.get());
            }
            return resultMessage;
        }
        return getErrorDeclinedMessage();
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String getErrorDeclinedMessage() {
        return ERROR_DECLINED_MESSAGE_PATTERN
                .replace("{admin}", administratorName);
    }

    private String getParticipantDeclinedGameMessage(ActiveGame activeGame, String triggeredEventOwner) {
        return PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN
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
