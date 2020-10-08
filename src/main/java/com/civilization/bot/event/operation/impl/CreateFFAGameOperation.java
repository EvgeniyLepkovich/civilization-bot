package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.validator.Validator;
import com.civilization.mapper.UserDtoMapper;
import com.civilization.model.ActiveGame;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.service.DrawTableService;
import com.civilization.service.UserService;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("createFFAGameOperation")
public class CreateFFAGameOperation implements EventOperation {

    private static final String FOOTER_MESSAGE_PATTERN_EN =
            "please, confirm participation putting {gameId}+ in game chat\n" +
                    "you can decline game putting {gameId}- in chat";

    private static final String FOOTER_MESSAGE_PATTERN_RU =
            "пожалуйста, подтвердите участие написав {gameId}+\n" +
                    "вы можете отклонить игру написав {gameId}-";

    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("userConnectedToSteamValidator")
    private Validator userConnectedToSteamValidator;
    @Autowired
    private DrawTableService drawTableService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @Override
    public String execute(MessageReceivedEvent event) throws Exception {
        String message = StringUtils.normalizeSpace(event.getMessage().getContentDisplay());
        List<User> users = userService.createFFAGameForUsers(getUsernames(message), getHost(event));
        checkAllUsersConnectedToSteam(event, users);
        return drawTableService.drawGameTable(userDtoMapper.toUsersDTO(users), Long.parseLong(getCurrentCreateGameId(users.get(0))));
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
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

    private List<String> getUsernames(String message) {
        return Stream.of(message.split("\\s"))
                .filter(command -> !("!ffa".equalsIgnoreCase(command) || "!ффа".equalsIgnoreCase(command)))
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
