package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("createFFAGameMessageRule")
public class CreateFFAGameMessageRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> message.startsWith("!ffa") && message.matches("[!]\\w+\\s@([A-Za-z0-9А-Яа-я\\(\\)\\_]).+");
    }
}
