package com.civilization.bot.event.validator;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;

public interface MessageValidator {
    boolean isRulesValid(String message, MessageListenedAppliedRule... rules);
}
