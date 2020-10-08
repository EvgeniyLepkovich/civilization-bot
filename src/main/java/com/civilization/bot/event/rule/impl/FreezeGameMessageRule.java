package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("FreezeGameMessageRule")
public class FreezeGameMessageRule  implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> message.matches("!freeze \\d+");
    }
}
