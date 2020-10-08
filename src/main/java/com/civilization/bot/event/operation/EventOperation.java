package com.civilization.bot.event.operation;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface EventOperation {
    String execute(MessageReceivedEvent event) throws Exception;
    //TODO: remove second method because SOLID
    MessageEmbed executeForMessageEmbed(MessageReceivedEvent event) throws Exception;
}
