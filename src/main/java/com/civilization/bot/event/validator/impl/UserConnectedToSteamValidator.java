package com.civilization.bot.event.validator.impl;

import com.civilization.bot.event.validator.Validator;
import com.civilization.exception.UserNotConnectedToSteamException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userConnectedToSteamValidator")
public class UserConnectedToSteamValidator implements Validator {
    @Override
    public void validate(Object object) {
        List<Member> members = (List<Member>) object;
        boolean anyNotConnectedToSteam = members.stream().anyMatch(member -> !getRoles(member).contains("рейтинг"));
        if (anyNotConnectedToSteam) {
            throw new UserNotConnectedToSteamException();
        }
    }

    private List<String> getRoles(Member member) {
        return member.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
