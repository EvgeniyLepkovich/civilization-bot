package com.civilization.bot.event.validator.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class MessageValidatorImpl implements MessageValidator {
    @Override
    public boolean isRulesValid(String message, MessageListenedAppliedRule... rules) {
        return Stream.of(rules).allMatch(rule -> rule.getRule().test(message));
    }
}
