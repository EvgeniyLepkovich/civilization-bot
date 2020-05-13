package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("sendMassMessageFromChannelToUsersRule")
public class SendMassMessageFromChannelToUsersRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> message.startsWith("!send") && message.split("\\s").length > 1;
    }
}
