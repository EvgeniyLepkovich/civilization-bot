package com.civilization.repository;

import com.civilization.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "select * from user as u where u.username in (?1)", nativeQuery = true)
    List<User> findByUsernameInList(String usernames);

    @Query(value = "select rating from user where username = ?1", nativeQuery = true)
    Long findCurrentRating(String username);
}
