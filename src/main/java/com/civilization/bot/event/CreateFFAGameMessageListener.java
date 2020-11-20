package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.configuration.custom.annotation.RatingGamesChannelEventAnnotation;
import com.civilization.dto.LobbyDto;
import com.civilization.exception.CodedException;
import com.civilization.service.LobbyService;
import com.civilization.util.GameIdParser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@DiscordMessageListenerQualifier
@RatingGamesChannelEventAnnotation
public class CreateFFAGameMessageListener extends BaseMessageListener {

    private EventOperation eventOperation;
    private GameIdParser gameIdParser;
    private LobbyService lobbyService;

    @Autowired
    protected CreateFFAGameMessageListener(
            MessageValidator messageValidator,
            @Qualifier("createFFAGameOperation") EventOperation eventOperation,
            @Qualifier("createFFAGameMessageRule") MessageListenedAppliedRule messageRule,
            GameIdParser gameIdParser,
            LobbyService lobbyService) {
        super(messageValidator, eventOperation, messageRule);

        this.gameIdParser = gameIdParser;
        this.eventOperation = eventOperation;
        this.lobbyService = lobbyService;
    }

    @Override
    protected void sendMessage(MessageReceivedEvent event) throws Exception {
        try {
            String table = eventOperation.execute(event);
            Message message = event.getChannel().sendMessage("```" + table + "```").complete(true);
            lobbyService.createLobby(new LobbyDto(gameIdParser.getGameId(table), message.getId()));
        } catch (CodedException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}
