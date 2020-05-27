package com.civilization.bot.event.rule.impl;

import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component("createFFAReportMessageRule")
public class CreateFFAReportMessageRule implements MessageListenedAppliedRule {
    @Override
    public Predicate<String> getRule() {
        return message -> (message.matches("[!]report\\s\\d+\\swinner:\\s@[A-Za-z0-9А-Яа-я\\(\\)\\_\\.].+")
                || message.matches("[!]репорт\\s\\d+\\sпобедитель:\\s@[A-Za-z0-9А-Яа-я\\(\\)\\_\\.].+"));
    }
}
