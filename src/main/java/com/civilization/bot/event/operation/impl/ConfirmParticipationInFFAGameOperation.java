package com.civilization.bot.event.operation.impl;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.ClearConfirmMessagesAfterGameStartedEvent;
import com.civilization.bot.event.operation.EventOperation;
import com.civilization.model.ActiveGame;
import com.civilization.service.ActiveGameService;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

@Component("confirmParticipationInFFAGameOperation")
public class ConfirmParticipationInFFAGameOperation implements EventOperation {

    private static final String GAME_STARTED_MESSAGE_PATTERN_FOR_ALL_PARTICIPANTS =
            "{users} - the game was started!\n" +
            "Game's id: {gameId}\n" +
            "Start time: {startTime}\n" +
            "The game owner should create report after game finished";

    private static final String GAME_REGISTERED_MESSAGE_PATTERN =
            "@{triggeredEventOwner} confirmed participation in game {gameId}!\n";

    private static final String ERROR_REGISTRATION_PATTERN = //temporal, until not moved to exception codes
        "Had some troubles including: @{triggeredEventOwner}, in the game: {gameId}!\n";

    @Autowired
    private ActiveGameService activeGameService;
    @Autowired
    private UpdateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation updateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation;
    @Autowired
    private ClearConfirmMessagesAfterGameStartedEvent clearConfirmMessagesAfterGameStartedEvent;

    @Override
    public String execute(MessageReceivedEvent event) throws RateLimitedException {
        String triggeredEventOwner = getTriggeredEventOwner(event);
        Long gameId = getGameId(event.getMessage().getContentDisplay());
        Optional<ActiveGame> activeGame = activeGameService.setUserConfirmedGame(gameId, triggeredEventOwner);

        if (!activeGame.isPresent()) {
            return generateExceptionMessage(gameId, triggeredEventOwner);
        }

        if (activeGame.get().isStarted()) {
            clearConfirmMessagesAfterGameStartedEvent.execute(gameId, event);
        }

        String resultMessage = generateGameStartMessage(activeGame.get(), triggeredEventOwner);
        updateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation.updateGameMessage(activeGame.get());
        return resultMessage;
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String generateGameStartMessage(ActiveGame activeGame, String triggeredEventOwner) {
        String usersInGame = getUsersInGameAsList(activeGame);
        return activeGame.isStarted()
                ? generateGameStartMessageForAllParticipant(activeGame, usersInGame)
                : generateMessageForOneParticipant(activeGame, triggeredEventOwner);
    }

    private String generateMessageForOneParticipant(ActiveGame activeGame, String triggeredEventOwner) {
        return GAME_REGISTERED_MESSAGE_PATTERN
                .replace("{triggeredEventOwner}", triggeredEventOwner)
                .replace("{gameId}", String.valueOf(activeGame.getId()));
    }

    private String generateGameStartMessageForAllParticipant(ActiveGame activeGame, String usersInGame) {
        return GAME_STARTED_MESSAGE_PATTERN_FOR_ALL_PARTICIPANTS
                .replace("{users}", usersInGame)
                .replace("{gameId}", String.valueOf(activeGame.getId()))
                .replace("{startTime}", activeGame.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
    }

    private String generateExceptionMessage(Long activeGameId, String triggeredEventOwner) {
        return ERROR_REGISTRATION_PATTERN
                .replace("{triggeredEventOwner}", triggeredEventOwner)
                .replace("{gameId}", String.valueOf(activeGameId));
    }

    private Long getGameId(String message) {
        return Long.valueOf(message.replaceAll("\\D", ""));
    }

    private String getUsersInGameAsList(ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .map(uag -> uag.getUser().getUsername())
                .collect(Collectors.joining(", "));
    }

    private String getTriggeredEventOwner(MessageReceivedEvent event) {
        return event.getMessage().getAuthor().getName();
    }
}
