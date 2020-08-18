package com.civilization.service.impl;

import com.civilization.model.UserRank;
import com.civilization.repository.RatingRepository;
import com.civilization.service.DrawTableService;
import com.civilization.service.RatingTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RatingTableServiceImpl implements RatingTableService {

    @Autowired
    private DrawTableService drawTableService;
    @Autowired
    private RatingRepository ratingRepository;

    private static final Comparator<UserRank> userCompetitiveTableComparator = new Comparator<UserRank>() {
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
    public String getUsersRankTable(int limit) {
        List<UserRank> allUsersRanks = ratingRepository.findAllUsersRanks(limit);
        allUsersRanks.sort(userCompetitiveTableComparator.reversed());
        return drawTableService.drawAllUsersRatingTable(allUsersRanks);
    }
}
