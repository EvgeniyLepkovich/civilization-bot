package com.civilization.bot.event.rule;

import java.util.function.Predicate;

public interface MessageListenedAppliedRule {
    Predicate<String> getRule();
}
