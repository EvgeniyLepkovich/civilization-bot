package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;

import com.civilization.exception.CodedException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;

public class BaseMessageListener extends ListenerAdapter {

    private final MessageValidator messageValidator;
    private final EventOperation eventOperation;
    private final MessageListenedAppliedRule messageRule;

    protected BaseMessageListener(MessageValidator messageValidator, EventOperation eventOperation, MessageListenedAppliedRule messageRule) {
        this.messageValidator = messageValidator;
        this.eventOperation = eventOperation;
        this.messageRule = messageRule;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!isEventInAvailableChannel(event)) {
            return;
        }
        if (!messageValidator.isRulesValid(event.getMessage().getContentDisplay(), messageRule)) {
            return;
        }

        try {
            sendMessage(event);
        } catch (CodedException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(MessageReceivedEvent event) throws Exception {
        String message = eventOperation.execute(event);
        if (StringUtils.isNotBlank(message)) {
            event.getChannel().sendMessage(message).queue();
        }
    }

    private boolean isEventInAvailableChannel(MessageReceivedEvent event) {
        String channelName = event.getChannel().getName();

        //TODO: REMOVE THIS FUCKING SHIT MOVING IT TO THE VALIDATORS, FUCKING IDIOT
        return "notification".equals(channelName) ||
                "рейтинговые-игры".equals(channelName) ||
                "рейтинговые-отчеты".equals(channelName);
    }
}
