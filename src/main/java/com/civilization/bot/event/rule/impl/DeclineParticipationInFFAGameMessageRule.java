package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("declineParticipationInFFAGameMessageRule")
public class DeclineParticipationInFFAGameMessageRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> message.matches("\\d+\\-");
    }
}
