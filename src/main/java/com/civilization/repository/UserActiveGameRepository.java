package com.civilization.repository;

import com.civilization.model.UserActiveGame;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActiveGameRepository extends CrudRepository<UserActiveGame, Long> {
}
