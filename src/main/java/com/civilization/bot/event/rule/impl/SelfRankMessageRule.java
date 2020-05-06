package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("SelfRankMessageRule")
public class SelfRankMessageRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> message.split("\\s+").length == 1
                && (message.equalsIgnoreCase("!rank") || message.equalsIgnoreCase("!рейтинг"));
    }
}
