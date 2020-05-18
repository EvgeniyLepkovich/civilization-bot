package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.dto.GameResultDTO;
import com.civilization.mapper.decorator.GameResultsMapper;
import com.civilization.service.UserService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import java.awt.*;
import java.util.List;

@Component("createFFAReportOperation")
public class CreateFFAReportOperation implements EventOperation {

    public static final String ADMIN_ROLE = "админ";
    private static final String TITLE_MESSAGE_EN =
            "The game №%s was finished! Results are present below:";
    private static final String TITLE_MESSAGE_RU =
            "Игра №%s закончена! Результаты представлены ниже:";
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
        boolean isEnglish = event.getMessage().getContentDisplay().contains("!report");
        return withResultMessage(results, isEnglish);
    }

    private boolean isAdmin(MessageReceivedEvent event) {
        return event.getMember().getRoles().stream()
                .anyMatch(role -> ADMIN_ROLE.equalsIgnoreCase(role.getName()));
    }

    private String getEventOwner(MessageReceivedEvent event) {
        return event.getAuthor().getName();
    }

    private MessageEmbed withResultMessage(List<GameResultDTO> gameResults, boolean isEnglish) {
        EmbedBuilder builder = new EmbedBuilder();
        generateTopic(gameResults, builder, isEnglish);
        generateFields(gameResults, builder, isEnglish);
        return builder.build();
    }

    private void generateFields(List<GameResultDTO> gameResults, EmbedBuilder builder, boolean isEnglish) {
        for (int i = 0; i < gameResults.size(); i++) {
            boolean shouldInline = gameResults.size() / 3 == 2;
            GameResultDTO concreteResult = gameResults.get(i);
            builder.addField(concreteResult.getUsername(), withResultMessageForSingleResult(concreteResult, isEnglish), shouldInline);
        }
    }

    private void generateTopic(List<GameResultDTO> gameResults, EmbedBuilder builder, boolean isEnglish) {
        builder
                .setTitle(String.format((isEnglish ? TITLE_MESSAGE_EN : TITLE_MESSAGE_RU), gameResults.get(0).getGameId() + ""))
                .setColor(Color.green);
    }

    private String withResultMessageForSingleResult(GameResultDTO gameResult, boolean isEnglish) {
        String result;
        if (isEnglish) {
            result = gameResult.getGameResult().toString() + "\n" +
                    "old rating: " + gameResult.getOldRating() + "\n" +
                    "new rating: " + gameResult.getNewRating() + "(" + (gameResult.getNewRating() - gameResult.getOldRating()) + ")";
        } else {
            result = gameResult.getGameResult().getRussian() + "\n" +
                    "старый рейтинг: " + gameResult.getOldRating() + "\n" +
                    "новый рейтинг: " + gameResult.getNewRating() + "(" + (gameResult.getNewRating() - gameResult.getOldRating()) + ")";
        }
        return result;
    }
}
