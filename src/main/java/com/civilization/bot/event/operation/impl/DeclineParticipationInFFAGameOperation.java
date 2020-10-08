package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.mapper.UserDtoMapper;
import com.civilization.model.ActiveGame;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.service.ActiveGameService;
import com.civilization.service.DrawTableService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("declineParticipationInFFAGameOperation")
public class DeclineParticipationInFFAGameOperation implements EventOperation {

    private static final String PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN_EN =
            "@{participant} declined the game number: {gameId}!\n" +
            "{participants} - your game was canceled";

    private static final String ERROR_DECLINED_MESSAGE_PATTERN_EN =
            "Had some troubles declining the game, please contact admin: @{admin}";

    @Autowired
    private ActiveGameService activeGameService;
    @Autowired
    private UpdateGameTableOperation updateGameTableOperation;
    @Autowired
    private DrawTableService drawTableService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @Value("${discord.administrator.name}")
    private String administratorName;

    @Override
    @Transactional
    public String execute(MessageReceivedEvent event) throws RateLimitedException {
        String triggeredEventOwner = getTriggeredEventOwner(event);
        String message = event.getMessage().getContentDisplay();
        Optional<ActiveGame> activeGameOptional = activeGameService.setUserDeclinedGame(getGameId(message), triggeredEventOwner);

        if (activeGameOptional.isPresent()) {
            ActiveGame activeGame = activeGameOptional.get();
            long gameId = activeGame.getId();
            updateGameTableOperation.updateGameMessage(drawTableService.drawGameDeclinedTable(userDtoMapper.toUsersDTO(toUsers(activeGame)), gameId), gameId);
            return getParticipantDeclinedGameMessage(activeGame, triggeredEventOwner);
        }
        return getErrorDeclinedMessage();
    }

    private List<User> toUsers(ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .map(UserActiveGame::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private String getErrorDeclinedMessage() {
        return ERROR_DECLINED_MESSAGE_PATTERN_EN
                .replace("{admin}", administratorName);
    }

    private String getParticipantDeclinedGameMessage(ActiveGame activeGame, String triggeredEventOwner) {
        return PARTICIPANT_DECLINED_GAME_MESSAGE_PATTERN_EN
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
