package com.civilization.bot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.List;

public class DiscordBot {

    private static final String TOKEN = "Njk4NjAyOTYwMDc4MzcyOTM0.XpIOtQ.WNSjQPc2vKpg78vfXrKqNl-Py_Y";
    private JDA botInstance;

    private DiscordBot() {
    }

    private void init() throws LoginException {
        this.botInstance =
                JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
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
