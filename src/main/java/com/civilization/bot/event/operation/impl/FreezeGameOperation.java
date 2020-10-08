package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.exception.NotEnoughPermissionsException;
import com.civilization.service.ActiveGameService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component("FreezeGameOperation")
public class FreezeGameOperation implements EventOperation {

    //TODO: move to the configuration table
    private static final String ADMIN_ROLE = "админ";

    @Autowired
    private ActiveGameService activeGameService;

    @Override
    public String execute(MessageReceivedEvent event) throws Exception {
        if (!isAdmin(event)) {
            throw new NotEnoughPermissionsException();
        }

        //TODO: move getMessage to the util
        String message = StringUtils.normalizeSpace(event.getMessage().getContentDisplay());
        Long gameId = getGameId(message);
        activeGameService.freezeGame(gameId);
        return getMessage(gameId);
    }

    private String getMessage(Long gameId) {
        return "game №" + gameId + " was frozen. Please, ask admin for the details";
    }

    //TODO: move to the util
    private Long getGameId(String message) {
        return Long.parseLong(message.split(" ")[1]);
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private boolean isAdmin(MessageReceivedEvent event) {
        return event.getMember().getRoles().stream()
                .anyMatch(role -> ADMIN_ROLE.equalsIgnoreCase(role.getName()));
    }
}
