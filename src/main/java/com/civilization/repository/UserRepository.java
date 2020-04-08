package com.civilization.repository;

import com.civilization.model.User;
import com.civilization.model.UserRank;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "select * from user as u where u.username in (?1)", nativeQuery = true)
    List<User> findByUsernameInList(String usernames);

    @Query(value =
            "select u.username, u.rating, count(gr.game_result_id) as gamesCount, " +
                    "(select count(*) from user u left join game_result gr on gr.user_id = u.user_id where gr.user_game_result = \"WINNER\" and username = ?1) as wins, " +
                    "(select count(*) from user u left join game_result gr on gr.user_id = u.user_id where gr.user_game_result = \"LEAVE\" and username = ?1) as leaves " +
                    "from user as u " +
                    "left join game_result gr on gr.user_id = u.user_id " +
                    "where username = ?1 " +
                    "group by u.username, u.rating ", nativeQuery = true)
    UserRank findUserRank(String username);

    @Query(value = "select rating from user where username = ?1", nativeQuery = true)
    Long findCurrentRating(String username);
}
