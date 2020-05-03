package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(MessageReceivedEvent event) throws Exception {
        event.getChannel().sendMessage(eventOperation.execute(event)).queue();
    }

    private boolean isEventInAvailableChannel(MessageReceivedEvent event) {
        String channelName = event.getChannel().getName();

        return "рейтинговые-игры".equals(channelName) ||
                "рейтинговые-отчеты".equals(channelName);
    }
}
