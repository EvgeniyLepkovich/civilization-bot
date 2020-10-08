package com.civilization.repository;

import com.civilization.model.User;
import com.civilization.model.UserRank;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<User, Long> {
    @Query(value =
            "select u.username, u.rating, count(gr.game_result_id) as gamesCount, " +
                    "(select count(*) from user u left join game_result gr on gr.user_id = u.user_id where gr.user_game_result = \"WINNER\" and username = ?1) as wins, " +
                    "(select count(*) from user u left join game_result gr on gr.user_id = u.user_id where gr.user_game_result = \"LEAVE\" and username = ?1) as leaves, " +
                    "(select count(*) from user u left join user_active_game uag on uag.user_id = u.user_id left join active_game ag on ag.active_game_id = uag.active_game_id where ag.game_status = \"SCRAP\" and username = ?1) as scrap " +
                    "from user as u " +
                    "left join game_result gr on gr.user_id = u.user_id " +
                    "where username = ?1 " +
                    "group by u.username, u.rating ", nativeQuery = true)
    UserRank findUserRank(String username);

    @Query(value =
            "select u.username, u.rating, count(gr.game_result_id) as gamesCount, " +
                    "(select count(game_result_id) from game_result gr where gr.user_game_result = \"WINNER\" and gr.user_id = u.user_id) as wins, " +
                    "(select count(game_result_id) from game_result gr where gr.user_game_result = \"LEAVE\" and gr.user_id = u.user_id) as leaves " +
                    "from user as u " +
                    "left join game_result gr on gr.user_id = u.user_id " +
                    "group by u.username, u.rating " +
                    "limit ?1", nativeQuery = true)
    List<UserRank> findAllUsersRanks(int limit);
}
