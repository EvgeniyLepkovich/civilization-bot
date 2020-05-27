package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("UserRankMessageRule")
public class UserRankMessageRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> (message.matches("[!]rank\\s@[A-Za-z0-9А-Яа-я\\(\\)\\_\\.]+") ||
                message.matches("[!]рейтинг\\s@[A-Za-z0-9А-Яа-я\\(\\)\\_\\.]+") );
    }
}
