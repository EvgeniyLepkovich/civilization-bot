package com.civilization.service.impl;

import com.civilization.model.UserRank;
import com.civilization.repository.UserRepository;
import com.civilization.service.RatingTableService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RatingTableServiceImpl implements RatingTableService {

    @Autowired
    private UserRepository userRepository;
    @Value("${bot.rating-channel-id}")
    private Long ratingChannelId;

    Comparator<UserRank> userCompetitiveTableComparator = new Comparator<UserRank>() {
        @Override
        public int compare(UserRank o1, UserRank o2) {
            if (isComparable(o1.getRating(), o2.getRating())) {
                return o1.getRating().compareTo(o2.getRating());
            }
            if (isComparable(o1.getGamesCount(), o2.getGamesCount())) {
                return o1.getGamesCount().compareTo(o2.getGamesCount());
            }
            if (isComparable(o1.getWins(), o2.getWins())) {
                return o1.getWins().compareTo(o2.getWins());
            }
            if (isComparable(o1.getLeaves(), o2.getLeaves())) {
                return o1.getLeaves().compareTo(o2.getLeaves()) * -1;
            }

            return 1;
        }

        private boolean isComparable(Long field1, Long field2) {
            return field1.compareTo(field2) != 0;
        }
    };

    @Override
    public void drawTable(JDA botInstance) {
        MessageEmbed message = getMessageEmbed();
        TextChannel textChannel = botInstance.getTextChannelById(ratingChannelId);
        Optional<List<Message>> oldMessages = getOldMessages(textChannel);
        if (oldMessages.isPresent() && oldMessages.get().size() > 0) {
            oldMessages.get().get(0).editMessage(message).queue();
        } else {
            textChannel.sendMessage(message).queue();
        }
    }

    private Optional<List<Message>> getOldMessages(TextChannel textChannel) {
        return Optional.ofNullable(textChannel.getHistory()).map(h -> h.retrievePast(1).complete());
    }

    private MessageEmbed getMessageEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        generateMessageHeaders(builder);
        List<UserRank> allUsers = getSortedUsers();
        generateMessageBody(builder, allUsers);
        return builder.build();
    }

    private void generateMessageBody(EmbedBuilder builder, List<UserRank> allUsers) {
        for (int i = 0; i < allUsers.size(); i++) {
            UserRank concreteUser = allUsers.get(i);
            builder.addField("", (i+1) + ". " + concreteUser.getUsername() + "  |  " + "points: " + concreteUser.getRating() +
                    "  |  " + "games: " + concreteUser.getGamesCount() + "  |  " + "wins: " + concreteUser.getWins() + "  |  " + "leaves: " + concreteUser.getLeaves(), false);
        }
    }

    private List<UserRank> getSortedUsers() {
        List<UserRank> allUsers = userRepository.findAllUsersRanks();
        allUsers.sort(userCompetitiveTableComparator.reversed());
        return allUsers;
    }

    private void generateMessageHeaders(EmbedBuilder builder) {
        builder.setColor(Color.GREEN);
    }
}
