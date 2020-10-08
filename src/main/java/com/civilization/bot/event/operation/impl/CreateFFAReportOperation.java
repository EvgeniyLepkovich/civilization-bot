package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.dto.GameResultDTO;
import com.civilization.mapper.GameResultsMapper;
import com.civilization.service.DrawTableService;
import com.civilization.service.UserService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import java.util.List;

@Component("createFFAReportOperation")
public class CreateFFAReportOperation implements EventOperation {

    public static final String ADMIN_ROLE = "админ";

    @Autowired
    private UserService userService;
    @Autowired
    private GameResultsMapper mapper;
    @Autowired
    private DrawTableService drawTableService;

    @Override
    public String execute(MessageReceivedEvent event) throws Exception {
        List<GameResultDTO> results = userService.createFFAReport(mapper.map(event.getMessage().getContentDisplay()), getEventOwner(event), isAdmin(event));
        return drawTableService.drawGameFinishedTable(results, results.get(0).getGameId());
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }

    private boolean isAdmin(MessageReceivedEvent event) {
        return event.getMember().getRoles().stream()
                .anyMatch(role -> ADMIN_ROLE.equalsIgnoreCase(role.getName()));
    }

    private String getEventOwner(MessageReceivedEvent event) {
        return event.getAuthor().getName();
    }
}
