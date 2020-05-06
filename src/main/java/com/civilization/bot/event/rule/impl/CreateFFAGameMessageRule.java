package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Predicate;

@Component("createFFAGameMessageRule")
public class CreateFFAGameMessageRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> (message.startsWith("!ffa") || message.startsWith("!ффа"))
                && message.matches("[!]([A-Za-z0-9А-Яа-я\\(\\)\\_])+\\s@([A-Za-z0-9А-Яа-я\\(\\)\\_]).+")
                && isSixUniquePlayers(message);
    }

    private boolean isSixUniquePlayers(String message) {
        return new HashSet<>(Arrays.asList(message.split(" "))).size() == 7;
    }
}
