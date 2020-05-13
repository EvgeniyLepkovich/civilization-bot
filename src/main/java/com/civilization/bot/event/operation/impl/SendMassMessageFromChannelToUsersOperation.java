package com.civilization.bot.event.operation.impl;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.validator.Validator;
import com.civilization.service.NotificationSenderService;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component("sendMassMessageFromChannelToUsersOperation")
public class SendMassMessageFromChannelToUsersOperation implements EventOperation {

    @Autowired
    @Qualifier("adminSendMassMessageValidator")
    private Validator validator;

    @Autowired
    private NotificationSenderService notificationSenderService;

    @Override
    public String execute(MessageReceivedEvent event) throws Exception {
        validator.validate(event);
        notificationSenderService.sendNotification(event.getJDA(), event.getMessage().getContentDisplay());
        return "";
    }

    @Override
    public MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception {
        throw new NotSupportedException();
    }
}
