package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.ClearConfirmMessagesAfterGameStartedEvent;
import com.civilization.bot.event.operation.EventOperation;
import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.model.ActiveGame;
import com.civilization.service.ActiveGameService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("confirmParticipationInFFAGameOperation")
public class ConfirmParticipationInFFAGameOperation implements EventOperation {

    private static final String GAME_STARTED_MESSAGE_PATTERN_FOR_ALL_PARTICIPANTS_EN =
            "{users} - the game was started!\n" +
            "Game's id: {gameId}\n" +
            "Start time: {startTime}\n" +
            "The game owner should create report after game finished";

    private static final String GAME_STARTED_MESSAGE_PATTERN_FOR_ALL_PARTICIPANTS_RU =
            "{users} - игра началась!\n" +
            "Id игры: {gameId}\n" +
            "Время начала: {startTime}\n" +
            "создатель события обязан подать репорт после окончания игры";

    private static final String GAME_REGISTERED_MESSAGE_PATTERN_EN =
            "@{triggeredEventOwner} confirmed participation in game {gameId}!\n";

    private static final String GAME_REGISTERED_MESSAGE_PATTERN_RU =
            "@{triggeredEventOwner} подтвердил участие в игре {gameId}!\n";

    private static final String ERROR_REGISTRATION_PATTERN_EN = //temporal, until not moved to exception codes
        "Had some troubles including: @{triggeredEventOwner}, in the game: {gameId}!\n";

    private static final String ERROR_REGISTRATION_PATTERN_RU = //temporal, until not moved to exception codes
        "проблемы подлючения у: @{triggeredEventOwner}, в игре: {gameId}!\n";

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
        boolean isEnglish = CreatedGameMessagesCache.getInstance().getLanguage(gameId.toString()).contains("isEnglish");
        Optional<ActiveGame> activeGame = activeGameService.setUserConfirmedGame(gameId, triggeredEventOwner);

        if (!activeGame.isPresent()) {
            return generateExceptionMessage(gameId, triggeredEventOwner, isEnglish);
        }


        String resultMessage = generateGameStartMessage(activeGame.get(), triggeredEventOwner, isEnglish);

        if (isEnglish) {
            updateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation.updateGameMessageEn(activeGame.get());
        } else {
            updateMessageOfCreateFFAGameAfterUserConfirmedParticipationOperation.updateGameMessageRu(activeGame.get());
        }

        if (activeGame.get().isStarted()) {
            clearConfirmMessagesAfterGameStartedEvent.execute(gameId, event);
            //remove started game from the waiting pool
            CreatedGameMessagesCache.getInstance().removeStartedGame(String.valueOf(activeGame.get().getId()));
        }
        return resultMessage;
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String generateGameStartMessage(ActiveGame activeGame, String triggeredEventOwner, boolean isEnglish) {
        String usersInGame = getUsersInGameAsList(activeGame);
        return activeGame.isStarted()
                ? generateGameStartMessageForAllParticipant(activeGame, usersInGame, isEnglish)
                : generateMessageForOneParticipant(activeGame, triggeredEventOwner, isEnglish);
    }

    private String generateMessageForOneParticipant(ActiveGame activeGame, String triggeredEventOwner, boolean isEnglish) {
        String gameRegisteredMessage = isEnglish ?
                GAME_REGISTERED_MESSAGE_PATTERN_EN :
                GAME_REGISTERED_MESSAGE_PATTERN_RU;
        return gameRegisteredMessage
                .replace("{triggeredEventOwner}", triggeredEventOwner)
                .replace("{gameId}", String.valueOf(activeGame.getId()));
    }

    private String generateGameStartMessageForAllParticipant(ActiveGame activeGame, String usersInGame, boolean isEnglish) {
        String gameStartedMessage = isEnglish ?
                GAME_STARTED_MESSAGE_PATTERN_FOR_ALL_PARTICIPANTS_EN :
                GAME_STARTED_MESSAGE_PATTERN_FOR_ALL_PARTICIPANTS_RU;
        return gameStartedMessage
                .replace("{users}", usersInGame)
                .replace("{gameId}", String.valueOf(activeGame.getId()))
                .replace("{startTime}", activeGame.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
    }

    private String generateExceptionMessage(Long activeGameId, String triggeredEventOwner, boolean isEnglish) {
        String exceptionMessage = isEnglish ?
                ERROR_REGISTRATION_PATTERN_EN :
                ERROR_REGISTRATION_PATTERN_RU;

        return exceptionMessage
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
