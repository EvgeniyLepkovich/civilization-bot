package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;

import com.civilization.exception.ChannelNotSupportedForEventException;
import com.civilization.exception.CodedException;
import com.civilization.exception.NoAvailableChannelsForEventException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Map;

public class BaseMessageListener extends ListenerAdapter {

    private final MessageValidator messageValidator;
    private final EventOperation eventOperation;
    private final MessageListenedAppliedRule messageRule;

    private String availableChannels;

    @Autowired
    @Qualifier("channelsByAnnotation")
    private Map<Class, String> channelsByAnnotation;

    protected BaseMessageListener(MessageValidator messageValidator, EventOperation eventOperation, MessageListenedAppliedRule messageRule) {
        this.messageValidator = messageValidator;
        this.eventOperation = eventOperation;
        this.messageRule = messageRule;
    }

    @PostConstruct
    public void init() {
        Annotation[] annotations = this.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            String channelName = channelsByAnnotation.get(annotation.annotationType());
            if (StringUtils.isNotBlank(channelName)) {
                availableChannels += channelName;
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!messageValidator.isRulesValid(event.getMessage().getContentDisplay(), messageRule)) {
            return;
        }

        try {
            checkAvailableChannel(event);
            sendMessage(event);
        } catch (CodedException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAvailableChannel(MessageReceivedEvent event) {
        if (StringUtils.isBlank(availableChannels)) {
            throw new NoAvailableChannelsForEventException("for event -> " + this.getClass().getSimpleName());
        }
        if (!availableChannels.contains(event.getChannel().getName())) {
            throw new ChannelNotSupportedForEventException("for event -> " + this.getClass().getSimpleName());
        }
    }

    protected void sendMessage(MessageReceivedEvent event) throws Exception {
        String message = eventOperation.execute(event);
        if (StringUtils.isNotBlank(message)) {
            event.getChannel().sendMessage(message).queue();
        }
    }
}
