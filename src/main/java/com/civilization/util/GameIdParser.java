package com.civilization.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GameIdParser {
    public String getGameId(String message) {
        Matcher matcher = Pattern.compile("№\\d+").matcher(message);
        matcher.find();
        return matcher.group().replace("№", "");
    }
}
