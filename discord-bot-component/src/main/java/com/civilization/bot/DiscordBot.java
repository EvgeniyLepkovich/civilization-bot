package com.civilization.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.List;

public class DiscordBot {

    private static final String TOKEN = "Njk4NjAyOTYwMDc4MzcyOTM0.XpIOtQ.qtLDFqkWvTFW6AXn0c0Hdjm9-1E";
    private JDA botInstance;

    private DiscordBot() {
    }

    private void init() throws LoginException {
        this.botInstance = new JDABuilder(AccountType.BOT)
                .setToken(TOKEN)
                .buildAsync();
    }

    public JDA getBotInstance() {
        if (botInstance == null) {
            try {
                init();
            } catch (LoginException e) {
                throw new RuntimeException();
            }
        }

        return botInstance;
    }

    public void addListeners(List<ListenerAdapter> listeners) {
        listeners.forEach(botInstance::addEventListener);
    }

    static class DiscordBotInstance {
        private static final DiscordBot INSTANCE = new DiscordBot();
    }

    public static DiscordBot getInstance() {
        return DiscordBotInstance.INSTANCE;
    }
}
