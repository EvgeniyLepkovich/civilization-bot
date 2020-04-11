package com.civilization.bot.event.operation.impl;

import java.awt.*;
import java.util.List;

import javax.transaction.NotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.dto.GameResultDTO;
//import com.civilization.mapper.GameResultsMapper;
import com.civilization.mapper.decorator.GameResultsMapper;
import com.civilization.service.UserService;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component("createFFAReportOperation")
public class CreateFFAReportOperation implements EventOperation {

    public static final String ADMIN_ROLE = "админ";
    @Autowired
    private UserService userService;
    @Autowired
    private GameResultsMapper mapper;

    @Override
    public String execute(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        List<GameResultDTO> results = userService.createFFAReport(mapper.map(event.getMessage().getContentDisplay()), getEventOwner(event), isAdmin(event));
        return withResultMessage(results);
    }

    private boolean isAdmin(MessageReceivedEvent event) {
        return event.getMember().getRoles().stream()
                .anyMatch(role -> ADMIN_ROLE.equalsIgnoreCase(role.getName()));
    }

    private String getEventOwner(MessageReceivedEvent event) {
        return event.getAuthor().getName();
    }

    private MessageEmbed withResultMessage(List<GameResultDTO> gameResults) {
        EmbedBuilder builder = new EmbedBuilder();
        generateTopic(gameResults, builder);
        generateFields(gameResults, builder);
        return builder.build();
    }

    private void generateFields(List<GameResultDTO> gameResults, EmbedBuilder builder) {
        for (int i = 0; i < gameResults.size(); i++) {
            boolean shouldInline = gameResults.size() / 3 == 2;
            GameResultDTO concreteResult = gameResults.get(i);
            builder.addField(concreteResult.getUsername(), withResultMessageForSingleResult(concreteResult), shouldInline);
        }
    }

    private void generateTopic(List<GameResultDTO> gameResults, EmbedBuilder builder) {
        builder
            .setTitle("The game №" + gameResults.get(0).getGameId() + "was finished! Results are present below:")
            .setColor(Color.green);
    }

    private String withResultMessageForSingleResult(GameResultDTO gameResult) {
        return
                gameResult.getGameResult().toString() + "\n" +
                "old rating: " + gameResult.getOldRating() + "\n" +
                "new rating: " + gameResult.getNewRating() + "(" + (gameResult.getNewRating() - gameResult.getOldRating()) + ")";
    }
}
