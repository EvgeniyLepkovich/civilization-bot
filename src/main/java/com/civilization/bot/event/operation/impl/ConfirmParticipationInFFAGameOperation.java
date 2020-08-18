package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.ClearConfirmMessagesAfterGameStartedEvent;
import com.civilization.bot.event.operation.EventOperation;
import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.dto.UserDTO;
import com.civilization.mapper.decorator.UserDtoMapper;
import com.civilization.model.ActiveGame;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.service.ActiveGameService;
import com.civilization.service.DrawTableService;
import com.civilization.service.RatingService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private UpdateGameTableOperation updateGameTableOperation;
    @Autowired
    private ClearConfirmMessagesAfterGameStartedEvent clearConfirmMessagesAfterGameStartedEvent;
    @Autowired
    private DrawTableService drawTableService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @Override
    @Transactional
    public String execute(MessageReceivedEvent event) throws RateLimitedException {
        String triggeredEventOwner = getTriggeredEventOwner(event);
        Long gameId = getGameId(event.getMessage().getContentDisplay());
        Optional<ActiveGame> activeGame = activeGameService.setUserConfirmedGame(gameId, triggeredEventOwner);

        if (!activeGame.isPresent()) {
            return generateExceptionMessage(gameId, triggeredEventOwner);
        }

        String resultMessage = generateGameStartMessage(activeGame.get(), triggeredEventOwner);

        if (activeGame.get().isStarted()) {
            String newGameTable = drawTableService.drawGameStartedTable(userDtoMapper.toUsersDTO(getUsersInGame(activeGame.get())), gameId);
            updateGameTableOperation.updateGameMessage(newGameTable, gameId);

            clearConfirmMessagesAfterGameStartedEvent.execute(gameId, event);
            //remove started game from the waiting pool
            CreatedGameMessagesCache.getInstance().removeStartedGame(String.valueOf(activeGame.get().getId()));
        } else {
            String newGameTable = drawTableService.drawGameTable(userDtoMapper.toUsersDTO(getUsersInGame(activeGame.get())), gameId);
            updateGameTableOperation.updateGameMessage(newGameTable, gameId);
        }
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

    private List<User> getUsersInGame(ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .map(UserActiveGame::getUser)
                .collect(Collectors.toList());
    }

    private String getTriggeredEventOwner(MessageReceivedEvent event) {
        return event.getMessage().getAuthor().getName();
    }
}
