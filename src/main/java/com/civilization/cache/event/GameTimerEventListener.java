package com.civilization.cache.event;

import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.cache.MessageGameIdPair;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiPredicate;

public class GameTimerEventListener implements EventListener {

    private static final long THIRTY_MINUTES_DELAY = 1800000L;

    private final GameMessageEvent activator = GameMessageEvent.NEW_MESSAGE_ADDED;
    private final BiPredicate<Message, MessageGameIdPair> messagesToDeleteCondition = (message, pair) ->
            message.getContentDisplay().equalsIgnoreCase(pair.getSecond() + "+") ||
            message.getContentDisplay().endsWith("confirmed participation in game " + pair.getSecond() + "!");

    @Override
    public void execute(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        if (!activator.equals(event)) {
            return;
        }

        TimerTask task = new TimerTask() {
            public void run() {
                CreatedGameMessagesCache.getInstance().removeMessage(messageGameIdPair.getSecond());
                List<Message> history = messageGameIdPair.getFirst().getChannel().getHistory().retrievePast(100).complete();
                history.stream()
                        .filter(message -> messagesToDeleteCondition.test(message, messageGameIdPair))
                        .forEach(message -> messageGameIdPair.getFirst().getChannel().deleteMessageById(message.getId()).complete());
            }
        };
        Timer timer = new Timer("DeleteMessagesAfterNTimer");
        timer.schedule(task, THIRTY_MINUTES_DELAY);
    }

    @Override
    public GameMessageEvent getActivator() {
        return activator;
    }
}
