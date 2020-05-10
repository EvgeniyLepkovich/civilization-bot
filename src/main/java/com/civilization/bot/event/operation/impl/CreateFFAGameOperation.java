package com.civilization.bot.event.operation.impl;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.transaction.NotSupportedException;

import com.civilization.bot.event.validator.Validator;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.model.ActiveGame;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.service.UserService;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component("createFFAGameOperation")
public class CreateFFAGameOperation implements EventOperation {

    private static final String FOOTER_MESSAGE_PATTERN =
            "please, confirm participation putting {gameId}+ in game chat\n" +
            "you can decline game putting {gameId}- in chat";

    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("userConnectedToSteamValidator")
    private Validator userConnectedToSteamValidator;

    @Override
    public String execute(MessageReceivedEvent event) throws NotSupportedException {
        throw new NotSupportedException();
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        String message = StringUtils.normalizeSpace(event.getMessage().getContentDisplay());
        List<User> users = userService.createFFAGameForUsers(getUsernames(message), getHost(event));
        checkAllUsersConnectedToSteam(event, users);
        return getCreateFFAGameMessage(users);
    }

    private void checkAllUsersConnectedToSteam(MessageReceivedEvent event, List<User> users) throws Exception {
        userConnectedToSteamValidator.validate(toMembers(event, users));
    }

    private List<Member> toMembers(MessageReceivedEvent event, List<User> users) {
        return users.stream()
                .map(user -> event.getGuild().getMembersByName(user.getUsername(), false))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private MessageEmbed getCreateFFAGameMessage(List<User> users) {
        String gameId = getCurrentCreateGameId(users.get(0));
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("ffa game №" + gameId + " was created")
                .setColor(Color.green);

        users.sort(Comparator.comparing(User::getUsername));
        users.forEach(user -> builder.addField("@" + user.getUsername(), "current rating: " + user.getRating() + "\nIs ready: " + toEmojy(isUserConfirmedGame(gameId, user)), true));
        String footerMessage = FOOTER_MESSAGE_PATTERN.replaceAll("\\{gameId}", gameId);
        return builder.setFooter(footerMessage, null).build();
    }

    private String toEmojy(boolean isReady) {
        return isReady ? ":partying_face:" : ":rage:";
    }

    private boolean isUserConfirmedGame(String gameId, User user) {
        return user.getUserActiveGames().stream()
                .anyMatch(uag -> uag.getActiveGame().getId() == Long.valueOf(gameId) && uag.isGameConfirmed());
    }

    private List<String> getUsernames(String message) {
        return Stream.of(message.split("\\s"))
                .filter(command -> !"!ffa".equalsIgnoreCase(command))
                .map(word -> word.replace("@", ""))
                .collect(Collectors.toList());
    }

    private String getHost(MessageReceivedEvent event) {
        return event.getAuthor().getName();
    }

    private String getCurrentCreateGameId(User user) {
        return user.getUserActiveGames().stream()
                .map(UserActiveGame::getActiveGame)
                .max(Comparator.comparing(ActiveGame::getId))
                .map(ActiveGame::getId)
                .map(String::valueOf)
                .orElse("-1L");
    }
}
