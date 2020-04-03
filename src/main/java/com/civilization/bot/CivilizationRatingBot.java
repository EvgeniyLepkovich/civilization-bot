package com.civilization.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class CivilizationRatingBot {

    private JDA botInstance;

    public JDA initBot(String token) throws LoginException {
        this.botInstance = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .buildAsync();

        return botInstance;
    }

    public JDA getBotInstance() throws Exception {
        if (botInstance == null) {
            throw new Exception("bot is not initialized");
        }
        return botInstance;
    }
}
